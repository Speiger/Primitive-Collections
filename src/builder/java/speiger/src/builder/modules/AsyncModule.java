package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class AsyncModule extends BaseModule
{
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
	protected void loadBlockades() {
		if(!isModuleEnabled()) {
			addBlockedFiles("AsyncBuilder");
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
