package speiger.src.builder.dependencies;

import speiger.src.builder.ClassType;
import speiger.src.builder.modules.BaseModule;

@SuppressWarnings("javadoc")
public class ModuleDependency extends BaseDependency {
	BaseModule owner;
	
	public ModuleDependency(BaseModule owner, boolean biType) {
		super(owner.getModuleName(), biType);
		this.owner = owner;
	}
	
	public FunctionDependency createDependency(String name) {
		FunctionDependency result = new FunctionDependency(this, name);
		if(biType) result.addEntryDependency(this);
		else result.addKeyDependency(this);
		return result;
	}
	
	@Override
	public LoadingState isLoaded(ClassType key, ClassType value) {
		if(dependencies == null) return LoadingState.REQUIRED;
		LoadingState result = getLocalState(key, value);
		if(FETCH_FAILURES && result == LoadingState.REJECTED) {
			FAILURE_KEYS.add(getLocalStateKey(key, value));
		}
		return result.replaceIfUndefined(getGlobalState()).resolveIfUndefined().merge(getReqirementState(key, value));
	}
}
