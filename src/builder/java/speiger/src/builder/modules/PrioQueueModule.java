package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependency.DependencyFunction;
import speiger.src.builder.dependency.DependencyModule;
import speiger.src.builder.dependency.DependencyModule.SingleTypeModule;

@SuppressWarnings("javadoc")
public class PrioQueueModule extends BaseModule
{
	public static final BaseModule INSTANCE = new PrioQueueModule();
	public static final DependencyModule MODULE = CollectionModule.MODULE.addChild(new SingleTypeModule(INSTANCE));
	public static final DependencyFunction IMPLEMENTATION = MODULE.createFunction("Implementations");
	public static final DependencyFunction WRAPPERS = MODULE.createFunction("Wrappers");
	public static final DependencyFunction DEQUEUE = MODULE.createFunction("Dequeue");
	public static final DependencyFunction FIFO_QUEUE = DEQUEUE.addChild(IMPLEMENTATION.createSubFunction("FiFoQueue"));
	public static final DependencyFunction HEAP_QUEUE = IMPLEMENTATION.createSubFunction("HeapQueue");
	public static final DependencyFunction ARRAY_PRIO_QUEUE = IMPLEMENTATION.createSubFunction("ArrayPrioQueue");
	
	@Override
	public String getModuleName() { return "PriorityQueue"; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFunctions() {}
	@Override
	public boolean areDependenciesLoaded() { return isDependencyLoaded(CollectionModule.INSTANCE); }
	
	@Override
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType) {
		return new TreeSet<>(Arrays.asList("Wrappers", "Implementations", "Dequeue", "FiFoQueue", "HeapQueue", "ArrayPrioQueue"));
	}
	
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
