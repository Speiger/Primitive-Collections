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
	protected void loadFlags() {}
	
	@Override
	protected void loadClasses()
	{
		//Abstract Classes
		addAbstractMapper("BASE_TASK", "Base%sTask");
		
		//Interfaces
		addClassMapper("TASK", "Task");
	}
}
