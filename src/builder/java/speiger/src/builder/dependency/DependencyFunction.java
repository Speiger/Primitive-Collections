package speiger.src.builder.dependency;

import java.util.Arrays;

import com.google.gson.JsonObject;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public abstract class DependencyFunction extends DependencyBase {
	protected DependencyModule owner;
	
	public DependencyFunction(DependencyModule owner) {
		this.owner = owner;
	}
	
	@Override
	public boolean isEnabled() {
		return getState(owner.owner.keyType(), owner.owner.valueType()) == LoadingState.LOADED;
	}
	
	public abstract DependencyFunction createSubFunction(String function);
	
	public static class SingleFunction extends DependencyFunction {
		LoadingState[] state = new LoadingState[ClassType.values().length];
		String functionName;
		
		public SingleFunction(DependencyModule owner, String functionName) {
			super(owner);
			this.functionName = functionName;
			Arrays.fill(state, LoadingState.UNDEFINED);
		}
		
		@Override
		public DependencyFunction createSubFunction(String function) {
			return addChild(new SingleFunction(owner, function));
		}
		
		@Override
		public void load(JsonObject obj, ClassType keyType, ClassType valueType) {
			state[keyType.ordinal()] = owner.isFunctionLoaded(obj, keyType, valueType, functionName);
		}

		@Override
		public LoadingState getState(ClassType keyType, ClassType valueType) {
			return state[keyType.ordinal()];
		}
		
		@Override
		public boolean resolveDependencies() {
			boolean returnType = false;
			for(ClassType type : ClassType.values()) {
				LoadingState state = this.state[type.ordinal()];
				if(state == LoadingState.UNLOADED) continue;
				state = state.merge(getChildState(type, type)).merge(getParentState(type, type));
				if(state != this.state[type.ordinal()]) {
					this.state[type.ordinal()] = state;
					returnType = true;
				}
			}
			return returnType;
		}
		
		@Override
		public String getName() {
			return functionName;
		}
		
		@Override
		public void setLoaded() {
			Arrays.fill(state, LoadingState.LOADED);
		}
	}
	
	public static class BiTypeFunction extends DependencyFunction {
		LoadingState[][] state = new LoadingState[ClassType.values().length][ClassType.values().length];
		String functionName;
		
		BiTypeFunction(DependencyModule owner, String functionName) {
			super(owner);
			this.functionName = functionName;
			for(int i = 0;i<state.length;i++) {
				Arrays.fill(state[i], LoadingState.UNDEFINED);
			}
		}
		
		@Override
		public DependencyFunction createSubFunction(String function) {
			return new BiTypeFunction(owner, function);
		}
		
		@Override
		public void load(JsonObject obj, ClassType keyType, ClassType valueType) {
			state[keyType.ordinal()][valueType.ordinal()] = owner.isFunctionLoaded(obj, keyType, valueType, functionName);
		}
		
		@Override
		public LoadingState getState(ClassType keyType, ClassType valueType) {
			return state[keyType.ordinal()][valueType.ordinal()];
		}
		
		@Override
		public boolean resolveDependencies() {
			boolean returnType = false;
			for(ClassType keyType : ClassType.values()) {
				for(ClassType valueType : ClassType.values()) {
					LoadingState state = this.state[keyType.ordinal()][valueType.ordinal()];
					if(state == LoadingState.UNLOADED) continue;
					state = state.merge(getChildState(keyType, valueType)).merge(getParentState(keyType, valueType));
					if(state != this.state[keyType.ordinal()][valueType.ordinal()]) {
						this.state[keyType.ordinal()][valueType.ordinal()] = state;
						returnType = true;
					}
				}
			}
			return returnType;
		}
		
		@Override
		public String getName() {
			return functionName;
		}
		
		@Override
		public void setLoaded() {
			for(int i = 0;i<state.length;i++) {
				Arrays.fill(state[i], LoadingState.LOADED);
			}
		}
	}
}
