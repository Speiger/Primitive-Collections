package speiger.src.builder.modules;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class SetModule extends BaseModule
{
	@Override
	protected void loadVariables()
	{
		loadClasses();
		loadFunctions();
		loadReamppers();
		loadBlockades();
	}
	
	@Override
	protected void loadFlags()
	{
		
	}
	
	private void loadBlockades()
	{
		if(keyType == ClassType.BOOLEAN)
		{
			addBlockedFiles("SortedSet", "NavigableSet", "AVLTreeSet", "RBTreeSet");
			addBlockedFiles("OrderedSet", "ArraySet", "LinkedOpenHashSet", "LinkedOpenCustomHashSet");
			addBlockedFiles("OpenHashSet", "OpenCustomHashSet", "ImmutableOpenHashSet");
		}
	}
	
	private void loadReamppers()
	{
		addRemapper("AbstractSet", "Abstract%sSet");
		addRemapper("ImmutableOpenHashSet", "Immutable%sOpenHashSet");
	}
	
	private void loadFunctions()
	{
		addFunctionMapper("POLL_FIRST_KEY", "pollFirst");
		addFunctionMapper("POLL_LAST_KEY", "pollLast");
		addFunctionMapper("FIRST_KEY", "first");
		addFunctionMapper("LAST_KEY", "last");
	}
	
	private void loadClasses()
	{
		//Implementation Classes
		addClassMapper("LINKED_CUSTOM_HASH_SET", "LinkedOpenCustomHashSet");
		addClassMapper("LINKED_HASH_SET", "LinkedOpenHashSet");
		addAbstractMapper("IMMUTABLE_HASH_SET", "Immutable%sOpenHashSet");
		addClassMapper("CUSTOM_HASH_SET", "OpenCustomHashSet");
		addClassMapper("HASH_SET", "OpenHashSet");
		addClassMapper("RB_TREE_SET", "RBTreeSet");
		addClassMapper("AVL_TREE_SET", "AVLTreeSet");
		addClassMapper("ARRAY_SET", "ArraySet");
		
		//Abstract Classes
		addAbstractMapper("ABSTRACT_SET", "Abstract%sSet");
		
		//Helper Classes
		addClassMapper("SETS", "Sets");
		
		//Interfaces
		addClassMapper("NAVIGABLE_SET", "NavigableSet");
		addClassMapper("SORTED_SET", "SortedSet");
		addClassMapper("ORDERED_SET", "OrderedSet");
		addClassMapper("SET", "Set");
	}
}
