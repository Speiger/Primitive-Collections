package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class AsyncModule extends BaseModule
{
	public static final BaseModule INSTANCE = new AsyncModule();
	
	@Override
	public String getModuleName() { return "Async"; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadRemappers() {}
	@Override
	protected void loadTestClasses() {}
	@Override
	protected void loadFunctions() {}
	@Override
	public boolean areDependenciesLoaded() { return isDependencyLoaded(CollectionModule.INSTANCE); }
	@Override
	protected void loadBlockades() {
		if(!isModuleEnabled()) {
			addBlockedFiles("AsyncBuilder", "Task");
		}
	}
	@Override
	protected void loadFlags() {
		if(isModuleEnabled()) {
			addKeyFlag("ASYNC_MODULE");
		}
	}
	
	@Override
	protected void loadClasses()
	{
		//Implementation Classes
		addClassMapper("ASYNC_BUILDER", "AsyncBuilder");
		
		//Abstract Classes
		addAbstractMapper("BASE_TASK", "Base%sTask");
		
		//Interfaces
		addClassMapper("TASK", "Task");
	}
}
