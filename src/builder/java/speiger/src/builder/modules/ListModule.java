package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class ListModule extends BaseModule
{
	
	@Override
	protected void loadVariables()
	{
		loadClasses();
		loadFunctions();
		loadRemappers();
	}
	
	@Override
	protected void loadFlags()
	{
		
	}
	
	private void loadRemappers()
	{
		addRemapper("AbstractList", "Abstract%sList");
		addRemapper("ImmutableList", "Immutable%sList");
		addRemapper("CopyOnWriteList", "CopyOnWrite%sArrayList");
	}
	
	private void loadFunctions()
	{
		addFunctionMapper("GET_KEY", "get");
		addFunctionMapper("REMOVE_LAST", "removeLast");
		addFunctionMapper("REMOVE_SWAP", "swapRemove");
		addFunctionMappers("REPLACE", keyType.isObject() ? "replaceObjects" : "replace%ss");
		addFunctionMappers("SORT", "sort%ss");
	}
	
	private void loadClasses()
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
}
