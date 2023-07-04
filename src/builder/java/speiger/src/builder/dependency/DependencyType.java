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
		if(keyType != myType || valueType != myType) return LoadingState.UNDEFINED;
		return owner.getState(keyType, valueType);
	}
	
	@Override
	public boolean resolveDependencies() { return false; }
	@Override
	public boolean isEnabled() {
		return owner.getState(myType, myType) == LoadingState.LOADED;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public void setLoaded() {
		
	}
}
