package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

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
	protected void loadFunctions() {}
	@Override
	public boolean areDependenciesLoaded() { return isDependencyLoaded(CollectionModule.INSTANCE); }
	
	@Override
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType) {
		return new TreeSet<>(Arrays.asList("Wrappers", "Implementations", "Dequeue", "FiFoQueue", "HeapQueue", "ArrayPrioQueue"));
	}
	
	@Override
	protected void loadFlags() {
		if(isModuleEnabled()) addFlag("QUEUE_MODULE");
		if(isModuleEnabled("Wrappers")) addKeyFlag("QUEUES_FEATURE");
		boolean implementations = isModuleEnabled("Implementations");
		if(isModuleEnabled("Dequeue")) {
			addKeyFlag("DEQUEUE_FEATURE");
			if(implementations && isModuleEnabled("FiFoQueue")) addKeyFlag("FIFO_QUEUE_FEATURE");
		}
		if(implementations && isModuleEnabled("HeapQueue")) addKeyFlag("HEAP_QUEUE_FEATURE");
		if(implementations && isModuleEnabled("ArrayPrioQueue")) addKeyFlag("ARRAY_QUEUE_FEATURE");
	}
	
	@Override
	protected void loadBlockades() {
		if(!isModuleEnabled()) addBlockedFiles("PriorityQueue", "AbstractPriorityQueue");
		if(!isModuleEnabled("Wrappers")) addBlockedFiles("PriorityQueues");
		boolean implementations = !isModuleEnabled("Implementations");
		boolean dequeue = !isModuleEnabled("Dequeue");
		if(dequeue) addBlockedFiles("PriorityDequeue"); 
		if(dequeue || implementations || !isModuleEnabled("FiFoQueue")) addBlockedFiles("ArrayFIFOQueue");
		if(implementations || !isModuleEnabled("HeapQueue")) addBlockedFiles("HeapPriorityQueue");
		if(implementations || !isModuleEnabled("ArrayPrioQueue")) addBlockedFiles("ArrayPriorityQueue");
		
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
