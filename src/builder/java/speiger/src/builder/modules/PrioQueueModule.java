package speiger.src.builder.modules;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class PrioQueueModule extends BaseModule
{
	public static final BaseModule INSTANCE = new PrioQueueModule();
	
	@Override
	public String getModuleName() { return "PriorityQueue"; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFlags() {}
	@Override
	protected void loadFunctions() {}
	@Override
	public boolean areDependenciesLoaded() { return isDependencyLoaded(CollectionModule.INSTANCE); }
	@Override
	protected void loadBlockades()
	{
		if(keyType == ClassType.BOOLEAN) {
			addBlockedFiles("QueueTests");
		}
	}
	
	@Override
	protected void loadRemappers()
	{
		//Main Classes
		addRemapper("AbstractPriorityQueue", "Abstract%sPriorityQueue");
		
		//Test Classes
		addRemapper("TestQueueGenerator", "Test%sQueueGenerator");
		addRemapper("AbstractQueueTester", "Abstract%sQueueTester");
		addRemapper("SimpleQueueTestGenerator", "Simple%sQueueTestGenerator");
	}
	
	@Override
	protected void loadClasses()
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
	
	@Override
	protected void loadTestClasses() 
	{
		//Implementation Classes
		addClassMapper("DEQUEUE_TEST_BUILDER", "DequeueTestSuiteBuilder");
		addClassMapper("QUEUE_TEST_BUILDER", "QueueTestSuiteBuilder");
		addClassMapper("QUEUE_TESTS", "QueueTests");
		
		//Abstract Classes
		addAbstractMapper("ABSTRACT_QUEUE_TESTER", "Abstract%sQueueTester");
		
		//Helper Classes
		addAbstractMapper("SIMPLE_QUEUE_TEST_GENERATOR", "Simple%sQueueTestGenerator");
		addAbstractMapper("TEST_QUEUE_GENERATOR", "Test%sQueueGenerator");
	}
}
