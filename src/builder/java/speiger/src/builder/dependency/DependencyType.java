package speiger.src.builder.dependency;

import com.google.gson.JsonObject;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class DependencyType extends DependencyBase {
	DependencyBase owner;
	ClassType myType;
	
	public DependencyType(DependencyBase owner, ClassType myType) {
		this.owner = owner;
		this.myType = myType;
	}

	@Override
	public void load(JsonObject obj, ClassType keyType, ClassType valueType) {}
	
	@Override
	public LoadingState getState(ClassType keyType, ClassType valueType) {
		return owner.getState(myType, myType);
	}
	
	@Override
	public boolean resolveDependencies() { return false; }
	@Override
	public boolean isEnabled() {
		return owner.getState(myType, myType) == LoadingState.LOADED;
	}
}
