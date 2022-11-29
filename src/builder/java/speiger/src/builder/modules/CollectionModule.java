package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class CollectionModule extends BaseModule
{
	@Override
	protected void loadVariables()
	{
		loadClasses();
		loadFunctions();
		loadRemappers();
		loadBlockades();
	}
	
	@Override
	protected void loadFlags()
	{
	}
	
	private void loadBlockades()
	{
		if(keyType.isObject()) addBlockedFiles("Stack");
	}
	
	private void loadRemappers()
	{
		addRemapper("IArray", "I%sArray");
		addRemapper("AbstractCollection", "Abstract%sCollection");
	}
	
	private void loadFunctions()
	{
		addFunctionMapper("NEXT", "next");
		addSimpleMapper("NEW_STREAM", keyType.isPrimitiveBlocking() ? "" : keyType.getCustomJDKType().getKeyType()+"Stream");
		addFunctionMapper("PREVIOUS", "previous");
		addFunctionMapper("REMOVE_KEY", "rem");
		addSimpleMapper("TO_ARRAY", "to"+keyType.getNonFileType()+"Array");
		addSimpleMapper("VALUE_TO_ARRAY", "to"+valueType.getNonFileType()+"Array");
	}
	
	private void loadClasses()
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
		addClassMapper("STACK", "Stack");
		addClassMapper("STRATEGY", "Strategy");
	}
	
}
