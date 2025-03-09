package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.List;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependencies.FunctionDependency;
import speiger.src.builder.dependencies.IDependency;
import speiger.src.builder.dependencies.ModuleDependency;

@SuppressWarnings("javadoc")
public class PrioQueueModule extends BaseModule
{
	public static final BaseModule INSTANCE = new PrioQueueModule();
	public static final ModuleDependency MODULE = new ModuleDependency(INSTANCE, false).addKeyDependency(CollectionModule.MODULE);
	public static final FunctionDependency IMPLEMENTATION = MODULE.createDependency("Implementations");
	public static final FunctionDependency WRAPPERS = MODULE.createDependency("Wrappers");
	public static final FunctionDependency DEQUEUE = MODULE.createDependency("Dequeue");
	
	public static final FunctionDependency FIFO_QUEUE = MODULE.createDependency("FiFoQueue").addKeyDependency(DEQUEUE).addKeyDependency(IMPLEMENTATION);
	public static final FunctionDependency HEAP_QUEUE = MODULE.createDependency("HeapQueue").addKeyDependency(IMPLEMENTATION);
	public static final FunctionDependency ARRAY_PRIO_QUEUE = MODULE.createDependency("ArrayPrioQueue").addKeyDependency(IMPLEMENTATION);
	
	
	@Override
	public String getModuleName() { return "PriorityQueue"; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFunctions() {}
	@Override
	public List<IDependency> getDependencies(ClassType keyType, ClassType valueType) { return Arrays.asList(MODULE, WRAPPERS, IMPLEMENTATION, DEQUEUE, FIFO_QUEUE, HEAP_QUEUE, ARRAY_PRIO_QUEUE); }
	
	@Override
	protected void loadFlags() {
		if(MODULE.isEnabled()) addFlag("QUEUE_MODULE");
		if(WRAPPERS.isEnabled()) addKeyFlag("QUEUES_FEATURE");
		if(DEQUEUE.isEnabled()) addKeyFlag("DEQUEUE_FEATURE");
		if(FIFO_QUEUE.isEnabled()) addKeyFlag("FIFO_QUEUE_FEATURE");
		if(HEAP_QUEUE.isEnabled()) addKeyFlag("HEAP_QUEUE_FEATURE");
		if(ARRAY_PRIO_QUEUE.isEnabled()) addKeyFlag("ARRAY_QUEUE_FEATURE");
	}
	
	@Override
	protected void loadBlockades() {
		if(!MODULE.isEnabled()) addBlockedFiles("PriorityQueue", "AbstractPriorityQueue");
		if(!WRAPPERS.isEnabled()) addBlockedFiles("PriorityQueues");
		if(!DEQUEUE.isEnabled()) addBlockedFiles("PriorityDequeue");
		if(!FIFO_QUEUE.isEnabled()) addBlockedFiles("ArrayFIFOQueue");
		if(!HEAP_QUEUE.isEnabled()) addBlockedFiles("HeapPriorityQueue");
		if(!ARRAY_PRIO_QUEUE.isEnabled()) addBlockedFiles("ArrayPriorityQueue");
		
		if(keyType == ClassType.BOOLEAN) {
			addBlockedFiles("QueueTests");
		}
	}
	
	@Override
	protected void loadRemappers() {
		//Main Classes
		addRemapper("AbstractPriorityQueue", "Abstract%sPriorityQueue");
		
		//Test Classes
		addRemapper("TestQueueGenerator", "Test%sQueueGenerator");
		addRemapper("AbstractQueueTester", "Abstract%sQueueTester");
		addRemapper("SimpleQueueTestGenerator", "Simple%sQueueTestGenerator");
	}
	
	@Override
	protected void loadClasses() {
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
