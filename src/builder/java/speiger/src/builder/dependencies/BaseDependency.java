package speiger.src.builder.dependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.function.Consumer;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependencies.Requirements.Requirement;

@SuppressWarnings("javadoc")
public abstract class BaseDependency implements IDependency {
	protected static boolean FETCH_FAILURES = false;
	protected static Set<String> FAILURE_KEYS = new TreeSet<>();
	
	protected final String name;
	protected final boolean biType;
	protected Map<String, LoadingState> dependencies;
	protected List<IDependency> children = new ArrayList<>();
	protected List<Requirement> requirements = new ArrayList<>();
	protected ClassType keyType;
	protected ClassType valueType;
	
	public BaseDependency(String name, boolean biType) {
		this.name = name;
		this.biType = biType;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public void set(Map<String, LoadingState> dependency) {
		dependencies = dependency;
	}
	
	@Override
	public IDependency addDependency(Requirement require) {
		requirements.add(require);
		require.dependency.addChild(this);
		return this;
	}
	
	@Override
	public void addChild(IDependency child) {
		children.add(child);
	}
	
	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	@Override
	public boolean isRoot() {
		return requirements.isEmpty();
	}
	
	protected LoadingState getGlobalState() {
		return dependencies.getOrDefault(name, LoadingState.OPTIONAL);
	}
	
	@Override
	public String getLocalStateKey(ClassType keyType, ClassType valueType) {
		return (biType ? keyType.getClassPath()+"-"+valueType.getClassPath() : keyType.getClassPath())+"-"+name;
	}
	
	protected LoadingState getLocalState(ClassType keyType, ClassType valueType) {
		return dependencies.getOrDefault(getLocalStateKey(keyType, valueType), LoadingState.OPTIONAL);
	}
		
	protected LoadingState getReqirementState(ClassType keyType, ClassType valueType) {
		LoadingState state = requirements.isEmpty() ? LoadingState.REQUIRED : LoadingState.OPTIONAL;
		for(int i = 0,m=requirements.size();i<m;i++) {
			state = state.merge(requirements.get(i).test(keyType, valueType));
		}
		return state.resolveIfUndefined();
	}
	
	@Override
	public void resolveRequirements(ClassType keyType, ClassType valueType) {
		if(!children.isEmpty()) {
			for(IDependency child : children) {
				if(child == this) continue;
				child.resolveRequirements(keyType, valueType);
			}
		}
		if(getLocalState(keyType, valueType) == LoadingState.REQUIRED) {
			for(Requirement req : requirements) {
				dependencies.putIfAbsent(req.key(keyType, valueType), LoadingState.REQUIRED);
			}
		}
	}
	
	@Override
	public void validateDependency(Consumer<String> result, ClassType keyType, ClassType valueType) {
		if(getLocalState(keyType, valueType) == LoadingState.REQUIRED) {
			FETCH_FAILURES = true;
			for(Requirement req : requirements) {
				req.test(keyType, valueType);
			}
			FETCH_FAILURES = false;
			if(FAILURE_KEYS.size() > 0) {
				int size = FAILURE_KEYS.size();
				StringJoiner joiner = new StringJoiner("], [", "[", "]");
				FAILURE_KEYS.forEach(joiner::add);
				FAILURE_KEYS.clear();
				String joins = size > 1 ? "["+joiner.toString()+"]" : joiner.toString();
				
				result.accept("["+getLocalStateKey(keyType, valueType)+"] Requires "+joins+" but it specifically has been disabled!");
			}
			
		}
	}
	
	@Override
	public void set(ClassType key, ClassType value) {
		this.keyType = key;
		this.valueType = value;
	}
	
	@Override
	public boolean isEnabled() {
		if(keyType == null || keyType == null) return false;
		return isLoaded(keyType, valueType).getJsonResult();
	}
	
	@Override
	public String getName() {
		return name;
	}
}
