package speiger.src.builder.dependencies;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class FunctionDependency extends BaseDependency {
	ModuleDependency owner;
	
	public FunctionDependency(ModuleDependency owner, String name) {
		super(name, owner.biType);
		this.owner = owner;
	}
	
	@Override
	public LoadingState isLoaded(ClassType key, ClassType value) {
		if(dependencies == null) return LoadingState.REQUIRED;
		LoadingState result = getLocalState(key, value);
		if(FETCH_FAILURES && result == LoadingState.REJECTED) {
			FAILURE_KEYS.add(getLocalStateKey(key, value));
		}
		return result.resolveIfUndefined().merge(getReqirementState(key, value));
	}
	
	@Override
	public String getLocalStateKey(ClassType keyType, ClassType valueType) {
		return (biType ? keyType.getClassPath()+"-"+valueType.getClassPath() : keyType.getClassPath())+"-"+owner.getName()+"-"+name;
	}

}
