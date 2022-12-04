package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class ListModule extends BaseModule
{
	@Override
	public String getModuleName() { return "List"; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFlags() {
		if(isModuleEnabled()) addKeyFlag("LIST_MODULE");
		if(isModuleEnabled("Lists")) addKeyFlag("LISTS_FEATURE");
		if(isModuleEnabled("ArrayList")) addKeyFlag("ARRAY_LIST_FEATURE");
		if(isModuleEnabled("LinkedList")) addKeyFlag("LINKED_LIST_FEATURE");
		if(isModuleEnabled("ImmutableList")) addKeyFlag("IMMUTABLE_LIST_FEATURE");
		if(isModuleEnabled("CopyOnWriteList")) addKeyFlag("COPY_ON_WRITE_LIST_FEATURE");
	}
	
	@Override
	protected void loadBlockades()
	{
		if(!isModuleEnabled("Lists")) addBlockedFiles("Lists");
		if(!isModuleEnabled("ArrayList")) addBlockedFiles("ArrayList");
		if(!isModuleEnabled("LinkedList")) addBlockedFiles("LinkedList");
		if(!isModuleEnabled("ImmutableList")) addBlockedFiles("ImmutableList");
		if(!isModuleEnabled("CopyOnWriteList")) addBlockedFiles("CopyOnWriteList");
		if(!isModuleEnabled()) addBlockedFiles("List", "AbstractList");
		
		if(keyType.isObject()) addBlockedFiles("ListFillBufferTester");
		if(keyType == ClassType.BOOLEAN) addBlockedFiles("ListFillBufferTester", "ListReplaceAllTester");
	}
	
	@Override
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType)
	{
		return new HashSet<>(Arrays.asList("Lists", "ArrayList", "LinkedList", "ImmutableList", "CopyOnWriteList"));
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
		addFunctionMapper("REMOVE_LAST", "removeLast");
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