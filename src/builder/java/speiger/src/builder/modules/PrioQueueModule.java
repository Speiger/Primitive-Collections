package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class PrioQueueModule extends BaseModule
{
	@Override
	protected void loadVariables()
	{
		loadClasses();
		loadReampper();
	}
	
	@Override
	protected void loadFlags()
	{
		
	}
	
	private void loadReampper()
	{
		addRemapper("AbstractPriorityQueue", "Abstract%sPriorityQueue");
	}
		
	private void loadClasses()
	{
		//Implementation Classes
		addClassMapper("ARRAY_FIFO_QUEUE", "ArrayFIFOQueue");
		addClassMapper("ARRAY_PRIORITY_QUEUE", "ArrayPriorityQueue");
		addClassMapper("HEAP_PRIORITY_QUEUE", "HeapPriorityQueue");
		
		//Abstract Classes
		addAbstractMapper("ABSTRACT_PRIORITY_QUEUE", "Abstract%sPriorityQueue");
		
		//Helper Classes
		addClassMapper("PRIORITY_QUEUES", "PriorityQueues");
		
		//Interfaces
		addClassMapper("PRIORITY_QUEUE", "PriorityQueue");
		addClassMapper("PRIORITY_DEQUEUE", "PriorityDequeue");
		
	}
}
