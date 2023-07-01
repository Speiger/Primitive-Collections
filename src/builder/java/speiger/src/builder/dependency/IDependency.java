package speiger.src.builder.dependency;

import com.google.gson.JsonObject;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public interface IDependency {
	public void load(JsonObject obj, ClassType keyType, ClassType valueType);
	public LoadingState getState(ClassType keyType, ClassType valueType);
	public boolean isEnabled();
	
	public static enum LoadingState {
		UNDEFINED,
		LOADED,
		UNLOADED;
		
		public static LoadingState of(boolean value) {
			return value ? LOADED : UNLOADED;
		}
		
		public LoadingState merge(LoadingState merge) {
			return ordinal() > merge.ordinal() ? this : merge;
		}
		
		public LoadingState mergeDown(LoadingState merge) {
			if(merge == UNLOADED || ordinal() > merge.ordinal()) {
				return this;
			}
			return merge;
		}
		
		public LoadingState mergeUp(LoadingState merge) {
			if(merge == LOADED || ordinal() > merge.ordinal()) {
				return this;
			}
			return merge;
		}
	}
}
