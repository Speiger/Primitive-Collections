package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.List;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependencies.FunctionDependency;
import speiger.src.builder.dependencies.IDependency;
import speiger.src.builder.dependencies.ModuleDependency;

@SuppressWarnings("javadoc")
public class ListModule extends BaseModule
{
	public static final BaseModule INSTANCE = new ListModule();
	public static final ModuleDependency MODULE = new ModuleDependency(INSTANCE, false).addKeyDependency(CollectionModule.MODULE).addKeyDependency(CollectionModule.SPLIT_ITERATORS);
	public static final FunctionDependency IMPLEMENTATION = MODULE.createDependency("Implementations");
	public static final FunctionDependency WRAPPERS = MODULE.createDependency("Wrappers");
	public static final FunctionDependency ARRAY_LIST = MODULE.createDependency("ArrayList").addKeyDependency(IMPLEMENTATION);
	public static final FunctionDependency LINKED_LIST = MODULE.createDependency("LinkedList").addKeyDependency(IMPLEMENTATION);
	public static final FunctionDependency IMMUTABLE_LIST = MODULE.createDependency("ImmutableList").addKeyDependency(IMPLEMENTATION);
	public static final FunctionDependency COPY_ON_WRITE_LIST = MODULE.createDependency("CopyOnWriteList").addKeyDependency(IMPLEMENTATION);
	
	@Override
	public String getModuleName() { return "List"; }
	@Override
	public List<IDependency> getDependencies(ClassType keyType, ClassType valueType) { return Arrays.asList(MODULE, IMPLEMENTATION, WRAPPERS, ARRAY_LIST, LINKED_LIST, IMMUTABLE_LIST, COPY_ON_WRITE_LIST); }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFlags() {
		if(MODULE.isEnabled()) addKeyFlag("LIST_MODULE");
		if(WRAPPERS.isEnabled()) addKeyFlag("LISTS_FEATURE");
		if(ARRAY_LIST.isEnabled()) addKeyFlag("ARRAY_LIST_FEATURE");
		if(LINKED_LIST.isEnabled()) addKeyFlag("LINKED_LIST_FEATURE");
		if(IMMUTABLE_LIST.isEnabled()) addKeyFlag("IMMUTABLE_LIST_FEATURE");
		if(COPY_ON_WRITE_LIST.isEnabled()) addKeyFlag("COPY_ON_WRITE_LIST_FEATURE");
	}
	
	@Override
	protected void loadBlockades()
	{
		if(!WRAPPERS.isEnabled()) addBlockedFiles("Lists");
		if(!ARRAY_LIST.isEnabled()) addBlockedFiles("ArrayList");
		if(!LINKED_LIST.isEnabled()) addBlockedFiles("LinkedList");
		if(!IMMUTABLE_LIST.isEnabled()) addBlockedFiles("ImmutableList");
		if(!COPY_ON_WRITE_LIST.isEnabled()) addBlockedFiles("CopyOnWriteList");
		if(!MODULE.isEnabled()) addBlockedFiles("List", "AbstractList");
		
		
		if(keyType.isObject()) addBlockedFiles("ListFillBufferTester");
		if(keyType == ClassType.BOOLEAN) addBlockedFiles("ListFillBufferTester", "ListReplaceAllTester");
	}
	

	
	@Override
	protected void loadRemappers()
	{
		//Main Classes
		addRemapper("AbstractList", "Abstract%sList");
		addRemapper("ImmutableList", "Immutable%sList");
		addRemapper("CopyOnWriteList", "CopyOnWrite%sArrayList");
		
		//Test Classes
		addRemapper("AbstractListTester", "Abstract%sListTester");
		addRemapper("AbstractListIndexOfTester", "Abstract%sListIndexOfTester");
		addRemapper("TestListGenerator", "Test%sListGenerator");
	}
	
	@Override
	protected void loadFunctions()
	{
		addFunctionMapper("GET_KEY", "get");
		addFunctionMapper("GET_FIRST_KEY", "getFirst");
		addFunctionMapper("GET_LAST_KEY", "getLast");
		addFunctionMapper("REMOVE_FIRST_KEY", "removeFirst");
		addFunctionMapper("REMOVE_LAST_KEY", "removeLast");
		addFunctionMapper("REMOVE_SWAP", "swapRemove");
		addFunctionMappers("REPLACE", keyType.isObject() ? "replaceObjects" : "replace%ss");
		addFunctionMappers("SORT", "sort%ss");
	}
	
	@Override
	protected void loadClasses()
	{
		//Implementation Classes
		addClassMapper("ARRAY_LIST", "ArrayList");
		addAbstractMapper("COPY_ON_WRITE_LIST", "CopyOnWrite%sArrayList");
		addClassMapper("LINKED_LIST", "LinkedList");
		addAbstractMapper("IMMUTABLE_LIST", "Immutable%sList");
		
		//Abstract Classes
		addAbstractMapper("ABSTRACT_LIST", "Abstract%sList");
		
		//SubClasses
		addClassMapper("SUB_LIST", "SubList");
		addClassMapper("LIST_ITER", "ListIter");
		
		//Helper Classes
		addClassMapper("LISTS", "Lists");
		
		//Interfaces
		addClassMapper("LIST", "List");
	}
	
	@Override
	protected void loadTestClasses()
	{
		//Implementation Classes
		addClassMapper("LIST_TEST_BUILDER", "ListTestSuiteBuilder");
		addClassMapper("LIST_TESTS", "ListTests");

		//Abstract Classes
		addAbstractMapper("ABSTRACT_LIST_INDEX_OF_TESTER", "Abstract%sListIndexOfTester");
		addAbstractMapper("ABSTRACT_LIST_TESTER", "Abstract%sListTester");
		
		//Helper classes
		addAbstractMapper("TEST_LIST_GENERATOR", "Test%sListGenerator");
	}
}