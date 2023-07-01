package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependency.DependencyFunction;
import speiger.src.builder.dependency.DependencyModule;
import speiger.src.builder.dependency.DependencyModule.SingleTypeModule;

@SuppressWarnings("javadoc")
public class SetModule extends BaseModule
{
	public static final BaseModule INSTANCE = new SetModule();
	public static final DependencyModule MODULE = CollectionModule.MODULE.addChild(new SingleTypeModule(INSTANCE));
	public static final DependencyFunction WRAPPERS = MODULE.createFunction("Wrappers");
	public static final DependencyFunction IMPLEMENTATION = MODULE.createFunction("Implementations");
	public static final DependencyFunction ORDERED_SET = MODULE.createFunction("Wrappers");
	public static final DependencyFunction SORTED_SET = MODULE.createFunction("Wrappers");
	public static final DependencyFunction ARRAY_SET = ORDERED_SET.addChild(IMPLEMENTATION.createSubFunction("ArraySet"));
	public static final DependencyFunction IMMUTABLE_SET = IMPLEMENTATION.createSubFunction("ImmutableSet");
	public static final DependencyFunction HASH_SET = IMPLEMENTATION.createSubFunction("HashSet");
	public static final DependencyFunction LINKED_SET = HASH_SET.addChild(ORDERED_SET.addChild(IMPLEMENTATION.createSubFunction("LinkedHashSet")));
	public static final DependencyFunction CUSTOM_SET = IMPLEMENTATION.createSubFunction("CustomHashSet");
	public static final DependencyFunction LINKED_CUSTOM_SET = CUSTOM_SET.addChild(ORDERED_SET.addChild(IMPLEMENTATION.createSubFunction("LinkedCustomHashSet")));
	public static final DependencyFunction AVL_TREE_SET = SORTED_SET.addChild(IMPLEMENTATION.createSubFunction("AVLTreeSet"));
	public static final DependencyFunction RB_TREE_SET = SORTED_SET.addChild(IMPLEMENTATION.createSubFunction("RBTreeSet"));
	
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
		if(MODULE.isEnabled()) addFlag("SET_MODULE");
		if(WRAPPERS.isEnabled()) addFlag("SETS_FEATURE");
		if(ORDERED_SET.isEnabled()) addFlag("ORDERED_SET_FEATURE");
		if(SORTED_SET.isEnabled()) addFlag("SORTED_SET_FEATURE");
		if(IMMUTABLE_SET.isEnabled()) addFlag("IMMUTABLE_SET_FEATURE");
		if(ARRAY_SET.isEnabled()) addFlag("ARRAY_SET_FEATURE");
		if(HASH_SET.isEnabled()) addFlag("HASH_SET_FEATURE");
		if(LINKED_SET.isEnabled()) addFlag("LINKED_SET_FEATURE");
		if(CUSTOM_SET.isEnabled()) addFlag("CUSTOM_HASH_SET_FEATURE");
		if(LINKED_CUSTOM_SET.isEnabled()) addFlag("LINKED_CUSTOM_SET_FEATURE");
		if(AVL_TREE_SET.isEnabled()) addFlag("AVL_TREE_SET_FEATURE");
		if(RB_TREE_SET.isEnabled()) addFlag("RB_TREE_SET_FEATURE");
	}
	
	@Override
	protected void loadBlockades()
	{
		if(!MODULE.isEnabled()) addBlockedFiles("Set", "AbstractSet");
		if(!WRAPPERS.isEnabled()) addBlockedFiles("Sets");
		if(!IMMUTABLE_SET.isEnabled()) addBlockedFiles("ImmutableOpenHashSet");
		if(!ORDERED_SET.isEnabled()) addBlockedFiles("OrderedSet");
		if(!HASH_SET.isEnabled()) addBlockedFiles("OpenHashSet");
		if(!LINKED_SET.isEnabled()) addBlockedFiles("LinkedOpenHashSet");
		if(!CUSTOM_SET.isEnabled()) addBlockedFiles("OpenCustomHashSet");
		if(!LINKED_CUSTOM_SET.isEnabled()) addBlockedFiles("LinkedOpenCustomHashSet");
		if(!ARRAY_SET.isEnabled()) addBlockedFiles("ArraySet");
		if(!SORTED_SET.isEnabled()) addBlockedFiles("SortedSet", "NavigableSet");
		if(!AVL_TREE_SET.isEnabled()) addBlockedFiles("AVLTreeSet");
		if(!RB_TREE_SET.isEnabled()) addBlockedFiles("RBTreeSet");
		
		if(keyType == ClassType.BOOLEAN)
		{
			//Main Classes
			addBlockedFiles("SortedSet", "NavigableSet", "AVLTreeSet", "RBTreeSet");
			addBlockedFiles("OrderedSet", "ArraySet", "LinkedOpenHashSet", "LinkedOpenCustomHashSet");
			addBlockedFiles("Set", "Sets", "AbstractSet", "OpenHashSet", "OpenCustomHashSet", "ImmutableOpenHashSet");
			
			//Test Classes
			addBlockedFiles("SetTests", "SetTestSuiteBuilder", "TestSetGenerator");
			addBlockedFiles("OrderedSetTestSuiteBuilder", "TestOrderedSetGenerator", "OrderedSetMoveTester", "OrderedSetNavigationTester", "OrderedSetIterationTester");
			addBlockedFiles("SortedSetTestSuiteBuilder", "TestSortedSetGenerator", "SortedSetNaviationTester", "SortedSetSubsetTestSetGenerator", "SortedSetIterationTester", "SortedSetNaviationTester");
			addBlockedFiles("NavigableSetTestSuiteBuilder", "TestNavigableSetGenerator", "NavigableSetNavigationTester");
			addBlockedFiles("MinimalSet", "AbstractSetTester", "SetAddAllTester", "SetAddTester", "SetCreationTester", "SetEqualsTester", "SetRemoveTester");
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
