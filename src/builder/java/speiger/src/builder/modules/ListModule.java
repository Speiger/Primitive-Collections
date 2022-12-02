package speiger.src.builder.modules;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class ListModule extends BaseModule
{
	@Override
	public String getModuleName() { return "List"; }
	@Override
	protected void loadVariables() { loadBlockedFiles(); }
	@Override
	protected void loadFlags() {}
	
	private void loadBlockedFiles()
	{
		if(keyType.isObject()) {
			addBlockedFiles("ListFillBufferTester");
		}
		if(keyType == ClassType.BOOLEAN) {
			addBlockedFiles("ListFillBufferTester", "ListReplaceAllTester");
		}
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
		addClassMapper("ASYNC_BUILDER", "AsyncBuilder");
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