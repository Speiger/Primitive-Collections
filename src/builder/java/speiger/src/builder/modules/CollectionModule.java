package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.List;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependencies.FunctionDependency;
import speiger.src.builder.dependencies.IDependency;
import speiger.src.builder.dependencies.ModuleDependency;

@SuppressWarnings("javadoc")
public class CollectionModule extends BaseModule
{
	public static final BaseModule INSTANCE = new CollectionModule();
	public static final ModuleDependency MODULE = new ModuleDependency(INSTANCE, false)
			.addKeyDependency(FunctionModule.MODULE)
			.addOptionalTypeDependency(FunctionModule.MODULE, ClassType.OBJECT, true)
			.addOptionalTypeDependency(FunctionModule.MODULE, ClassType.INT, true)
			.addOptionalTypeDependency(ClassType.OBJECT, true);
	public static final FunctionDependency STREAMS = MODULE.createDependency("Streams");
	public static final FunctionDependency SPLIT_ITERATORS = MODULE.createDependency("Splititerators").addKeyDependency(STREAMS);
	public static final FunctionDependency IARRAY = MODULE.createDependency("IArray");
	public static final FunctionDependency STRATEGY = MODULE.createDependency("Strategy");
	
	@Override
	public String getModuleName() { return "Collection"; }
	@Override
	protected void loadVariables() {}
	@Override
	public List<IDependency> getDependencies(ClassType keyType, ClassType valueType) { return Arrays.asList(MODULE, STREAMS, SPLIT_ITERATORS, IARRAY, STRATEGY); }
	
	@Override
	protected void loadFlags() {
		if(MODULE.isEnabled()) addKeyFlag("COLLECTION_MODULE");
		if(STREAMS.isEnabled()) addKeyFlag("STREAM_FEATURE");
		if(SPLIT_ITERATORS.isEnabled()) addKeyFlag("SPLIT_ITERATOR_FEATURE");
		if(IARRAY.isEnabled()) addKeyFlag("IARRAY_FEATURE");
	}
	
	@Override
	protected void loadBlockades() {
		if(!MODULE.isEnabled()) {
			addBlockedFiles("Iterable", "Iterables", "Iterator", "Iterators", "BidirectionalIterator", "ListIterator");
			addBlockedFiles("Arrays", "Collection", "AbstractCollection", "Collections", "Stack");
		}
		if(!SPLIT_ITERATORS.isEnabled()) addBlockedFiles("Splititerator", "Splititerators");
		if(!IARRAY.isEnabled()) addBlockedFiles("IArray");
		if(!STRATEGY.isEnabled()) addBlockedFiles("Strategy");
		
		if(keyType.isObject())
		{
			addBlockedFiles("Stack");
			addBlockedFiles("CollectionStreamTester");
		}
		if(keyType == ClassType.BOOLEAN)
		{
			addBlockedFiles("CollectionRemoveIfTester", "CollectionStreamTester");
			addBlockedFilter(T -> T.endsWith("Tester") && T.startsWith("Iterable"));
		}
	}
	
	@Override
	protected void loadRemappers()
	{
		//Main Classes
		addRemapper("IArray", "I%sArray");
		addRemapper("AbstractCollection", "Abstract%sCollection");
		
		//Test Classes
		addRemapper("AbstractIteratorTester", "Abstract%sIteratorTester");
		addRemapper("MinimalCollection", "Minimal%sCollection");
		addRemapper("TestCollectionGenerator", "Test%sCollectionGenerator");
		addRemapper("AbstractContainerTester", "Abstract%sContainerTester");
		addRemapper("AbstractCollectionTester", "Abstract%sCollectionTester");
		addRemapper("SimpleTestGenerator", "Simple%sTestGenerator");
	}
	
	@Override
	protected void loadFunctions()
	{
		addFunctionMapper("NEXT", "next");
		addSimpleMapper("NEW_STREAM", keyType.isPrimitiveBlocking() ? "" : keyType.getCustomJDKType().getKeyType()+"Stream");
		addFunctionMapper("PREVIOUS", "previous");
		addFunctionMapper("REMOVE_KEY", "rem");
		addSimpleMapper("TO_ARRAY", "to"+keyType.getNonFileType()+"Array");
		addSimpleMapper("VALUE_TO_ARRAY", "to"+valueType.getNonFileType()+"Array");
	}
	
	@Override
	protected void loadClasses()
	{
		//Abstract Classes
		addAbstractMapper("ABSTRACT_COLLECTION", "Abstract%sCollection");
		
		//Helper Classes
		addClassMapper("ARRAYS", "Arrays");
		addClassMapper("COLLECTIONS", "Collections");
		addClassMapper("ITERABLES", "Iterables");
		addClassMapper("SPLIT_ITERATORS", "Splititerators");
		addClassMapper("ITERATORS", "Iterators");
		
		//Interfaces
		addClassMapper("COLLECTION", "Collection");
		addClassMapper("ITERABLE", "Iterable");
		addClassMapper("SPLIT_ITERATOR", "Splititerator");
		addClassMapper("LIST_ITERATOR", "ListIterator");
		addClassMapper("BI_ITERATOR", "BidirectionalIterator");
		addClassMapper("ITERATOR", "Iterator");
		if(keyType.isObject()) addSimpleMapper("STACK", "Stack");
		else addClassMapper("STACK", "Stack");
		addClassMapper("STRATEGY", "Strategy");
	}
	
	@Override
	protected void loadTestClasses()
	{
		//Implementation Classes
		addAbstractMapper("MINIMAL_COLLECTION", "Minimal%sCollection");
		addClassMapper("BIDIRECTIONAL_ITERATOR_TESTER", "BidirectionalteratorTester");
		addClassMapper("LIST_ITERATOR_TESTER", "ListIteratorTester");
		addClassMapper("ITERATOR_TESTER", "IteratorTester");
		addClassMapper("COLLECTION_TEST_BUILDER", "CollectionTestSuiteBuilder");
		addClassMapper("COLLECTION_CONSTRUCTOR_TESTS", "CollectionConstructorTests");
		
		//Abstract Classes
		addAbstractMapper("ABSTRACT_COLLECTION_TESTER", "Abstract%sCollectionTester");
		addAbstractMapper("ABSTRACT_CONTAINER_TESTER", "Abstract%sContainerTester");
		addAbstractMapper("ABSTRACT_ITERATOR_TESTER", "Abstract%sIteratorTester");
		
		//Helper Classes
		addAbstractMapper("TEST_COLLECTION_GENERATOR", "Test%sCollectionGenerator");
		addAbstractMapper("SIMPLE_TEST_GENERATOR", "Simple%sTestGenerator");

	}
}
