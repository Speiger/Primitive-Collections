package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class ListModule extends BaseModule
{
	public static final BaseModule INSTANCE = new ListModule();
	
	@Override
	public String getModuleName() { return "List"; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFlags() {
		if(isModuleEnabled()) addKeyFlag("LIST_MODULE");
		if(isModuleEnabled("Wrappers")) addKeyFlag("LISTS_FEATURE");
		boolean implementations = isModuleEnabled("Implementations");
		if(implementations && isModuleEnabled("ArrayList")) addKeyFlag("ARRAY_LIST_FEATURE");
		if(implementations && isModuleEnabled("LinkedList")) addKeyFlag("LINKED_LIST_FEATURE");
		if(implementations && isModuleEnabled("ImmutableList")) addKeyFlag("IMMUTABLE_LIST_FEATURE");
		if(implementations && isModuleEnabled("CopyOnWriteList")) addKeyFlag("COPY_ON_WRITE_LIST_FEATURE");
	}
	
	@Override
	protected void loadBlockades()
	{
		if(!isModuleEnabled("Wrappers")) addBlockedFiles("Lists");
		boolean implementations = !isModuleEnabled("Implementations");
		if(implementations || !isModuleEnabled("ArrayList")) addBlockedFiles("ArrayList");
		if(implementations || !isModuleEnabled("LinkedList")) addBlockedFiles("LinkedList");
		if(implementations || !isModuleEnabled("ImmutableList")) addBlockedFiles("ImmutableList");
		if(implementations || !isModuleEnabled("CopyOnWriteList")) addBlockedFiles("CopyOnWriteList");
		if(!isModuleEnabled()) addBlockedFiles("List", "AbstractList");
		
		if(keyType.isObject()) addBlockedFiles("ListFillBufferTester");
		if(keyType == ClassType.BOOLEAN) addBlockedFiles("ListFillBufferTester", "ListReplaceAllTester");
	}
	
	@Override
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType) {
		return new TreeSet<>(Arrays.asList("Implementations", "Wrappers", "ArrayList", "LinkedList", "ImmutableList", "CopyOnWriteList"));
	}
	
	@Override
	public boolean areDependenciesLoaded() {
		return isDependencyLoaded(CollectionModule.INSTANCE);
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