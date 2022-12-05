package speiger.src.builder.modules;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class SetModule extends BaseModule
{
	@Override
	public String getModuleName() { return "Set"; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFlags() {}
	
	@Override
	protected void loadBlockades()
	{
		if(keyType == ClassType.BOOLEAN)
		{
			//Main Classes
			addBlockedFiles("SortedSet", "NavigableSet", "AVLTreeSet", "RBTreeSet");
			addBlockedFiles("OrderedSet", "ArraySet", "LinkedOpenHashSet", "LinkedOpenCustomHashSet");
			addBlockedFiles("Set", "Sets", "AbstractSet", "OpenHashSet", "OpenCustomHashSet", "ImmutableOpenHashSet");
			
			//Test Classes
			addBlockedFiles("SetTests", "SetTestSuiteBuilder");
			addBlockedFiles("OrderedSetTestSuiteBuilder", "TestOrderedSetGenerator", "OrderedSetMoveTester", "OrderedSetNavigationTester", "OrderedMapNavigationTester", "OrderedMapTestSuiteBuilder", "OrderedSetIterationTester");
			addBlockedFiles("SortedSetTestSuiteBuilder", "TestSortedSetGenerator", "SortedSetNaviationTester", "SortedSetSubsetTestSetGenerator", "SortedSetIterationTester", "SortedSetNaviationTester");
			addBlockedFiles("NavigableSetTestSuiteBuilder", "TestNavigableSetGenerator", "NavigableSetNavigationTester");
		}
	}
	
	@Override
	protected void loadRemappers()
	{
		//Main Classes
		addRemapper("AbstractSet", "Abstract%sSet");
		addRemapper("ImmutableOpenHashSet", "Immutable%sOpenHashSet");
		
		//Test Classes
		addRemapper("MinimalSet", "Minimal%sSet");
		addRemapper("TestNavigableSetGenerator", "Test%sNavigableSetGenerator");
		addRemapper("TestSortedSetGenerator", "Test%sSortedSetGenerator");
		addRemapper("TestOrderedSetGenerator", "Test%sOrderedSetGenerator");
		addRemapper("TestSetGenerator", "Test%sSetGenerator");
		addRemapper("AbstractSetTester", "Abstract%sSetTester");
	}
	
	@Override
	protected void loadFunctions()
	{
		addFunctionMapper("POLL_FIRST_KEY", "pollFirst");
		addFunctionMapper("POLL_LAST_KEY", "pollLast");
		addFunctionMapper("FIRST_KEY", "first");
		addFunctionMapper("LAST_KEY", "last");
	}
	
	@Override
	protected void loadTestClasses()
	{
		//Implementation Classes
		addAbstractMapper("MINIMAL_SET", "Minimal%sSet");
		addClassMapper("ORDERED_SET_TEST_BUILDER", "OrderedSetTestSuiteBuilder");
		addClassMapper("SORTED_SET_TEST_BUILDER", "SortedSetTestSuiteBuilder");
		addClassMapper("NAVIGABLE_SET_TEST_BUILDER", "NavigableSetTestSuiteBuilder");
		addClassMapper("SET_TEST_BUILDER", "SetTestSuiteBuilder");
		addClassMapper("SET_TESTS", "SetTests");

		//Abstract Classes
		addAbstractMapper("ABSTRACT_SET_TESTER", "Abstract%sSetTester");
		
		//Helper Classes
		addClassMapper("SUB_SORTED_SET_CLASS_GENERATOR", "SortedSetSubsetTestSetGenerator");
		addClassMapper("SUB_NAVIGABLE_SET_CLASS_GENERATOR", "NavigableSetSubsetTestSetGenerator");
		addAbstractMapper("TEST_NAVIGABLE_SET_GENERATOR", "Test%sNavigableSetGenerator");
		addAbstractMapper("TEST_SORTED_SET_GENERATOR", "Test%sSortedSetGenerator");
		addAbstractMapper("TEST_ORDERED_SET_GENERATOR", "Test%sOrderedSetGenerator");
		addAbstractMapper("TEST_SET_GENERATOR", "Test%sSetGenerator");
	}
	
	@Override
	protected void loadClasses()
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
