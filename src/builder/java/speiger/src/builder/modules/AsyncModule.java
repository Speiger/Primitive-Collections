package speiger.src.builder.modules;

import speiger.src.builder.dependency.DependencyModule;
import speiger.src.builder.dependency.DependencyModule.SingleTypeModule;

@SuppressWarnings("javadoc")
public class AsyncModule extends BaseModule
{
	public static final BaseModule INSTANCE = new AsyncModule();
	public static final DependencyModule MODULE = CollectionModule.MODULE.addChild(new SingleTypeModule(INSTANCE));
	
	
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
		if(MODULE.isEnabled()) {
			addBlockedFiles("AsyncBuilder", "Task");
		}
	}
	@Override
	protected void loadFlags() {
		if(MODULE.isEnabled()) {
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
