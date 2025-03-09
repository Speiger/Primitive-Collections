package speiger.src.builder.dependencies;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependencies.Requirements.Requirement;

@SuppressWarnings("javadoc")
public interface IDependency {
	
	
	public void set(Map<String, LoadingState> dependency);
	public void set(ClassType key, ClassType value);
	public LoadingState isLoaded(ClassType key, ClassType value);
	public String getLocalStateKey(ClassType keyType, ClassType valueType);
	public boolean isEnabled();
	public boolean isLeaf();
	public boolean isRoot();
	
	public String getName();
	public void validateDependency(Consumer<String> result, ClassType keyType, ClassType valueType);
	public void resolveRequirements(ClassType keyType, ClassType valueType);
	
	public void addChild(IDependency child);
	public <T extends IDependency> T addDependency(Requirement require);
	public default <T extends IDependency> T addKeyDependency(IDependency dependency) { return addDependency(new Requirement(dependency, Requirements.KEY_TEST, Requirements.KEY_GETTER)); }
	public default <T extends IDependency> T addValueDependency(IDependency dependency) { return addDependency(new Requirement(dependency, Requirements.VALUE_TEST, Requirements.VALUE_GETTER)); }
	public default <T extends IDependency> T addEntryDependency(IDependency dependency) { return addDependency(new Requirement(dependency, Requirements.ENTRY_TEST, Requirements.ENTRY_GETTER)); }
	public default <T extends IDependency> T addTypeDependency(IDependency dependency, ClassType type) { return addDependency(new Requirement(dependency, Requirements.typedTest(type), Requirements.typedKey(type))); }
	public default <T extends IDependency> T addOptionalTypeDependency(IDependency dependency, ClassType type, boolean key) { return addDependency(new Requirement(dependency, Requirements.optionalTest(type, key), Requirements.optionalKey(type, key))); }
	public default <T extends IDependency> T addOptionalTypeDependency(ClassType type, boolean key) { return addDependency(new Requirement(this, Requirements.optionalTest(type, key), Requirements.optionalKey(type, key))); }

	
	public static void flatten(String prefix, boolean applyMiddle, JsonObject object, Map<String, LoadingState> result) {
		if(applyMiddle) prefix+="-";
		for(Entry<String, JsonElement> entry : object.entrySet()) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			if(value instanceof JsonPrimitive) {
				String entryKey = prefix+key;
				if("Enabled".equalsIgnoreCase(key)) {
					entryKey = prefix.substring(0, prefix.length()-1);
				}
				result.put(entryKey, LoadingState.of(((JsonPrimitive)value).getAsBoolean()));
			}
			if(value instanceof JsonObject) {
				flatten(prefix+key, true, (JsonObject)value, result);
			}
		}
	}
	
	public static enum LoadingState {
		OPTIONAL,
		REQUIRED,
		REJECTED;
		
		private static LoadingState RESOLVED = LoadingState.REQUIRED;
		
		public static LoadingState of(boolean value) {
			return value ? REQUIRED : REJECTED;
		}
		
		public LoadingState merge(LoadingState merge) {
			return ordinal() > merge.ordinal() ? this : merge;
		}
		
		public LoadingState replaceIfUndefined(LoadingState state) {
			return this == OPTIONAL ? state : this;
		}
		
		public LoadingState resolveIfUndefined() {
			return this == OPTIONAL ? RESOLVED : this;
		}
		
		public LoadingState mergeDown(LoadingState merge) {
			if(merge == REJECTED || ordinal() > merge.ordinal()) {
				return this;
			}
			return merge;
		}
		
		public LoadingState mergeUp(LoadingState merge) {
			if(merge == REQUIRED || ordinal() > merge.ordinal()) {
				return this;
			}
			return merge;
		}
		
		public static void setOptionalResolver(LoadingState state) {
			RESOLVED = state;
		}
		
		public boolean getJsonResult() {
			LoadingState state = this == OPTIONAL ? RESOLVED : this;
			return state == REQUIRED;
		}
	}
}
