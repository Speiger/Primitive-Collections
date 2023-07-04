package speiger.src.builder.dependency;

import java.util.Arrays;

import com.google.gson.JsonObject;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependency.DependencyFunction.BiTypeFunction;
import speiger.src.builder.dependency.DependencyFunction.SingleFunction;
import speiger.src.builder.modules.BaseModule;

@SuppressWarnings("javadoc")
public abstract class DependencyModule extends DependencyBase {
	protected BaseModule owner;
	
	public DependencyModule(BaseModule owner) {
		this.owner = owner;
	}
	
	public LoadingState isFunctionLoaded(JsonObject obj, ClassType keyType, ClassType valueType, String function) { return isModuleEnabled(obj, keyType, valueType, function); }
	
	public abstract DependencyFunction createFunction(String functionName);
	
	@Override
	public boolean isEnabled() {
		return getState(owner.keyType(), owner.valueType()) == LoadingState.LOADED;
	}
	
	public static class SingleTypeModule extends DependencyModule {
		LoadingState[] state = new LoadingState[ClassType.values().length];
		
		public SingleTypeModule(BaseModule owner) {
			super(owner);
			Arrays.fill(state, LoadingState.UNDEFINED);
		}
		
		@Override
		public DependencyFunction createFunction(String functionName) {
			return addChild(new SingleFunction(this, functionName));
		}
		
		@Override
		public void load(JsonObject obj, ClassType keyType, ClassType valueType) {
			state[keyType.ordinal()] = isModuleEnabled(obj, keyType, valueType);
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
			return "Enabled";
		}
		
		@Override
		public void setLoaded() {
			Arrays.fill(state, LoadingState.LOADED);
		}
	}
	
	public static class BiTypeModule extends DependencyModule {
		LoadingState[][] state = new LoadingState[ClassType.values().length][ClassType.values().length];

		public BiTypeModule(BaseModule owner) {
			super(owner);
			for(int i = 0;i<state.length;i++) {
				Arrays.fill(state[i], LoadingState.UNDEFINED);
			}
		}
		
		@Override
		public DependencyFunction createFunction(String functionName) {
			return addChild(new BiTypeFunction(this, functionName));
		}
		
		@Override
		public void load(JsonObject obj, ClassType keyType, ClassType valueType) {
			state[keyType.ordinal()][valueType.ordinal()] = isModuleEnabled(obj, keyType, valueType);
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
			return "Enabled";
		}
		
		@Override
		public void setLoaded() {
			for(int i = 0;i<state.length;i++) {
				Arrays.fill(state[i], LoadingState.LOADED);
			}
		}
	}
	
	protected LoadingState isModuleEnabled(JsonObject data, ClassType keyType, ClassType valueType) {
		LoadingState state = isEnabled(data, owner.getModuleName());
		JsonObject result = getObject(data, keyType.getClassPath(), false);
		state = state.replaceIfDefined(isEnabled(result, "Enabled"));
		if(owner.isBiModule()) {
			result = getObject(result, valueType.getClassPath(), false);
			state = state.replaceIfDefined(isEnabled(result, "Enabled"));
		}
		return state = state.replaceIfDefined(isEnabled(getObject(result, owner.getModuleName(), false), "Enabled"));
	}
	
	protected LoadingState isModuleEnabled(JsonObject data, ClassType keyType, ClassType valueType, String entry) {
		LoadingState state = isEnabled(data, owner.getModuleName());
		JsonObject result = getObject(data, keyType.getClassPath(), false);
		state = state.replaceIfDefined(isEnabled(result, "Enabled"));
		if(owner.isBiModule()) {
			result = getObject(result, valueType.getClassPath(), false);
			state = state.replaceIfDefined(isEnabled(result, "Enabled"));
		}
		result = getObject(result, owner.getModuleName(), false);
		return state.replaceIfDefined(isEnabled(result, "Enabled")).replaceIfDefined(isEnabled(result, entry));
	}

	private JsonObject getObject(JsonObject data, String name, boolean create) {
		JsonObject obj = data.getAsJsonObject(name);
		if(obj == null) {
			obj = new JsonObject();
			data.add(name, obj);
			if(create) obj.addProperty("Enabled", true);
		}
		return obj;
	}

	private LoadingState isEnabled(JsonObject obj, String key) {
		if (obj.has(key)) return LoadingState.of(obj.getAsJsonPrimitive(key).getAsBoolean());
		return LoadingState.UNDEFINED;
	}
}
