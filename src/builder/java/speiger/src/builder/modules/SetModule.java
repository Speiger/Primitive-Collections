package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class SetModule extends BaseModule
{
	public static final BaseModule INSTANCE = new SetModule();
	
	@Override
	public String getModuleName() { return "Set"; }
	@Override
	protected void loadVariables() {}
	
	@Override
	public boolean isModuleValid(ClassType keyType, ClassType valueType) { return keyType != ClassType.BOOLEAN; }
	@Override
	public boolean areDependenciesLoaded() { return isDependencyLoaded(CollectionModule.INSTANCE); }
	@Override
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType) {
		Set<String> sets = new TreeSet<>();
		sets.addAll(Arrays.asList("Wrappers", "Implementations"));
		sets.addAll(Arrays.asList("OrderedSet", "SortedSet"));
		sets.addAll(Arrays.asList("ArraySet", "ImmutableSet"));
		sets.addAll(Arrays.asList("HashSet", "LinkedHashSet"));
		sets.addAll(Arrays.asList("CustomHashSet", "LinkedCustomHashSet"));
		sets.addAll(Arrays.asList("AVLTreeSet", "RBTreeSet"));
		return sets;
	}
	
	@Override
	protected void loadFlags()
	{
		if(isModuleEnabled()) addFlag("SET_MODULE");
		if(isModuleEnabled("Wrappers")) addFlag("SETS_FEATURE");
		boolean implementations = isModuleEnabled("Implementations");
		boolean hashSet = implementations && isModuleEnabled("HashSet");
		boolean customHashSet = implementations && isModuleEnabled("CustomHashSet");
		
		if(isModuleEnabled("OrderedSet")) {
			addFlag("ORDERED_SET_FEATURE");
			if(implementations && isModuleEnabled("ArraySet")) addFlag("ARRAY_SET_FEATURE");
			if(hashSet && isModuleEnabled("LinkedHashSet")) addFlag("LINKED_SET_FEATURE");
			if(customHashSet && isModuleEnabled("LinkedCustomHashSet")) addFlag("LINKED_CUSTOM_SET_FEATURE");
		}
		if(isModuleEnabled("SortedSet")) {
			addFlag("SORTED_SET_FEATURE");
			if(implementations && isModuleEnabled("AVLTreeSet")) addFlag("AVL_TREE_SET_FEATURE");
			if(implementations && isModuleEnabled("RBTreeSet")) addFlag("RB_TREE_SET_FEATURE");
		}
		if(implementations && isModuleEnabled("ImmutableSet")) addFlag("IMMUTABLE_SET_FEATURE");
		if(hashSet) addFlag("HASH_SET_FEATURE");
		if(customHashSet) addFlag("CUSTOM_HASH_SET_FEATURE");
	}
	
	@Override
	protected void loadBlockades()
	{
		if(!isModuleEnabled()) addBlockedFiles("Set", "AbstractSet");
		if(!isModuleEnabled("Wrappers")) addBlockedFiles("Sets");
		boolean implementations = !isModuleEnabled("Implementations");
		if(implementations || !isModuleEnabled("ImmutableSet")) addBlockedFiles("ImmutableOpenHashSet");
		
		boolean ordered = !isModuleEnabled("OrderedSet");
		if(ordered) addBlockedFiles("OrderedSet");
		boolean hashSet = implementations || !isModuleEnabled("HashSet");
		if(hashSet) addBlockedFiles("OpenHashSet");
		if(hashSet || ordered || !isModuleEnabled("LinkedHashSet")) addBlockedFiles("LinkedOpenHashSet");
		
		boolean customHashSet = implementations || !isModuleEnabled("CustomHashSet");
		if(customHashSet) addBlockedFiles("OpenCustomHashSet");
		if(customHashSet || ordered || !isModuleEnabled("LinkedCustomHashSet")) addBlockedFiles("LinkedOpenCustomHashSet");
		
		if(implementations || ordered || !isModuleEnabled("ArraySet")) addBlockedFiles("ArraySet");
		
		boolean sorted = !isModuleEnabled("SortedSet");
		if(sorted) addBlockedFiles("SortedSet", "NavigableSet");
		if(implementations || sorted || !isModuleEnabled("AVLTreeSet")) addBlockedFiles("AVLTreeSet");
		if(implementations || sorted || !isModuleEnabled("RBTreeSet")) addBlockedFiles("RBTreeSet");
		
		if(keyType == ClassType.BOOLEAN)
		{
			//Main Classes
			addBlockedFiles("SortedSet", "NavigableSet", "AVLTreeSet", "RBTreeSet");
			addBlockedFiles("OrderedSet", "ArraySet", "LinkedOpenHashSet", "LinkedOpenCustomHashSet");
			addBlockedFiles("Set", "Sets", "AbstractSet", "OpenHashSet", "OpenCustomHashSet", "ImmutableOpenHashSet");
			
			//Test Classes
			addBlockedFiles("SetTests", "SetTestSuiteBuilder");
			addBlockedFiles("OrderedSetTestSuiteBuilder", "TestOrderedSetGenerator", "OrderedSetMoveTester", "OrderedSetNavigationTester", "OrderedSetIterationTester");
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
