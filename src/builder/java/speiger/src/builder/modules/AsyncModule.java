package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.List;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependency.DependencyModule;
import speiger.src.builder.dependency.DependencyModule.SingleTypeModule;
import speiger.src.builder.dependency.IDependency;

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
	public List<IDependency> getDependencies(ClassType keyType, ClassType valueType) { return Arrays.asList(MODULE); }
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
