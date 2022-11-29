package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class AsyncModule extends BaseModule
{
	@Override
	protected void loadVariables()
	{
		loadClasses();
	}
	
	@Override
	protected void loadFlags()
	{
		
	}
	
	private void loadClasses()
	{
		//Abstract Classes
		addAbstractMapper("BASE_TASK", "Base%sTask");
		
		//Interfaces
		addClassMapper("TASK", "Task");
	}
}
