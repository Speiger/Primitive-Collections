package speiger.src.builder.dependencies;

import java.util.function.Consumer;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependencies.IDependency.LoadingState;

@SuppressWarnings("javadoc")
public class Requirements {
	public static final RequirementTest KEY_TEST = (T, K, V) -> T.isLoaded(K, K);
	public static final RequirementTest VALUE_TEST = (T, K, V) -> T.isLoaded(V, V);
	public static final RequirementTest ENTRY_TEST = (T, K, V) -> T.isLoaded(K, V);
	
	public static RequirementTest typedTest(ClassType type) {
		return (T, K, V) -> T.isLoaded(type, type);
	}
	
	public static RequirementTest optionalTest(ClassType type, boolean key) {
		return (T, K, V) -> (key ? K : V) != type ? T.isLoaded(type, type) : LoadingState.REQUIRED;
	}
	
	public static final RequirementKey KEY_GETTER = (T, K, V) -> T.getLocalStateKey(K, K);
	public static final RequirementKey VALUE_GETTER = (T, K, V) -> T.getLocalStateKey(V, V);
	public static final RequirementKey ENTRY_GETTER = (T, K, V) -> T.getLocalStateKey(K, V);
	
	public static RequirementKey typedKey(ClassType type) {
		return (T, K, V) -> T.getLocalStateKey(type, type);
	}
	
	public static RequirementKey optionalKey(ClassType type, boolean key) {
		return (T, K, V) -> (key ? K : V) != type ? T.getLocalStateKey(type, type) : "";
	}

	
	
	public interface RequirementTest {
		public LoadingState test(IDependency test, ClassType keyType, ClassType valueType);
	}
	
	public static interface RequirementKey {
		public String key(IDependency test, ClassType keyType, ClassType valueType);
	}
	
	public static interface RequirementResolver {
		public void resolve(IDependency test, Consumer<String> result, ClassType keyType, ClassType valueType);
	}
	
	public static class Requirement {
		IDependency dependency;
		RequirementTest test;
		RequirementKey key;
		
		public Requirement(IDependency dependency, RequirementTest test, RequirementKey key) {
			this.dependency = dependency;
			this.test = test;
			this.key = key;
		}
		
		public LoadingState test(ClassType keyType, ClassType valueType) {
			return test.test(dependency, keyType, valueType);
		}
		
		public String key(ClassType keyType, ClassType valueType) {
			return key.key(dependency, keyType, valueType);
		}
		
	}
}
