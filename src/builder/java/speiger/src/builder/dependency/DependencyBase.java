package speiger.src.builder.dependency;

import java.util.ArrayList;
import java.util.List;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public abstract class DependencyBase implements IDependency {
	protected List<IDependency> children = new ArrayList<>();
	protected List<IDependency> parents = new ArrayList<>();

	public <T extends DependencyBase> T addChild(T child) {
		children.add(child);
		child.addParent(this);
		return child;
	}
	
	public <T extends DependencyBase> T addParent(DependencyBase parent) {
		parents.add(parent);
		return (T) this;
	}
	
	public List<IDependency> getParents() {
		return parents;
	}
	
	public List<IDependency> getChildren() {
		return children;
	}
	
	public abstract boolean resolveDependencies();
	
	protected LoadingState getChildState(ClassType keyType, ClassType valueType) {
		LoadingState state = LoadingState.UNDEFINED;
		for(IDependency child : children) {
			if(state == LoadingState.LOADED) return LoadingState.LOADED;
			state = state.mergeDown(child.getState(keyType, valueType));
		}
		return state;
	}
	
	protected LoadingState getParentState(ClassType keyType, ClassType valueType) {
		LoadingState state = LoadingState.UNDEFINED;
		for(IDependency parent : parents) {
			if(state == LoadingState.UNLOADED) return LoadingState.UNLOADED;
			state = state.mergeUp(parent.getState(keyType, valueType));
		}
		return state;
	}
}
