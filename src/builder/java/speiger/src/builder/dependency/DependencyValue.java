package speiger.src.builder.dependency;

import com.google.gson.JsonObject;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class DependencyValue extends DependencyBase {
	DependencyBase owner;
	
	public DependencyValue(DependencyBase owner) {
		this.owner = owner;
	}
	
	@Override
	public void load(JsonObject obj, ClassType keyType, ClassType valueType) {}

	@Override
	public LoadingState getState(ClassType keyType, ClassType valueType) {
		return owner.getState(valueType, keyType);
	}
	
	@Override
	public boolean resolveDependencies() {
		return false;
	}
	
	@Override
	public boolean isEnabled() {
		return owner.isEnabled();
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public void setLoaded() {		
	}
}
