package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class MapModule extends BaseModule
{
	public static final BaseModule INSTANCE = new MapModule();
	
	@Override
	public String getModuleName() { return "Map"; }
	@Override
	public boolean isBiModule() { return true; }
	@Override
	protected void loadVariables() {}
	@Override
	public boolean isModuleValid(ClassType keyType, ClassType valueType) { return keyType != ClassType.BOOLEAN; }
	@Override
	public boolean areDependenciesLoaded() { return isDependencyLoaded(SetModule.INSTANCE) && isDependencyLoaded(CollectionModule.INSTANCE, false); }
	
	@Override
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType) {
		Set<String> sets = new TreeSet<>();
		sets.addAll(Arrays.asList("Wrappers", "Implementations"));
		sets.addAll(Arrays.asList("OrderedMap", "SortedMap"));
		sets.addAll(Arrays.asList("ArrayMap", "ConcurrentMap", "ImmutableMap"));
		sets.addAll(Arrays.asList("HashMap", "LinkedHashMap"));
		sets.addAll(Arrays.asList("CustomHashMap", "LinkedCustomHashMap"));
		sets.addAll(Arrays.asList("EnumMap", "LinkedEnumMap"));
		sets.addAll(Arrays.asList("AVLTreeMap", "RBTreeMap"));
		return sets;
	}
	
	@Override
	protected void loadFlags()
	{
		if(isModuleEnabled()) addFlag("MAP_MODULE");
		if(isModuleEnabled("Wrappers")) addFlag("MAPS_FEATURE");
		boolean implementations = isModuleEnabled("Implementations");
		boolean hashMap = implementations && isModuleEnabled("HashMap");
		boolean customHashMap = implementations && isModuleEnabled("CustomHashMap");
		boolean enumMap = implementations && isModuleEnabled("EnumMap");
		
		if(isModuleEnabled("OrderedMap")) {
			addFlag("ORDERED_MAP_FEATURE");
			if(isModuleEnabled("ArrayMap")) addFlag("ARRAY_MAP_FEATURE");
			if(hashMap && isModuleEnabled("LinkedHashMap")) addFlag("LINKED_MAP_FEATURE");
			if(customHashMap && isModuleEnabled("LinkedCustomHashMap")) addFlag("LINKED_CUSTOM_MAP_FEATURE");
			if(enumMap && isModuleEnabled("LinkedEnumMap")) addFlag("LINKED_ENUM_MAP_FEATURE");
		}
		if(isModuleEnabled("SortedMap")) {
			addFlag("SORTED_MAP_FEATURE");
			if(implementations && isModuleEnabled("AVLTreeMap")) addFlag("AVL_TREE_MAP_FEATURE");
			if(implementations && isModuleEnabled("RBTreeMap")) addFlag("RB_TREE_MAP_FEATURE");
		}
		if(implementations && isModuleEnabled("ConcurrentMap")) addFlag("CONCURRENT_MAP_FEATURE");
		if(implementations && isModuleEnabled("ImmutableMap")) addFlag("IMMUTABLE_MAP_FEATURE");
		if(hashMap) addFlag("MAP_FEATURE");
		if(customHashMap) addFlag("CUSTOM_MAP_FEATURE");
		if(enumMap) addFlag("ENUM_MAP_FEATURE");
	}
	
	@Override
	protected void loadBlockades()
	{
		if(!isModuleEnabled()) addBlockedFiles("Map", "AbstractMap");
		if(!isModuleEnabled("Wrappers")) addBlockedFiles("Maps");
		boolean implementations = !isModuleEnabled("Implementations");
		if(implementations || !isModuleEnabled("ImmutableMap")) addBlockedFiles("ImmutableOpenHashMap");
		if(implementations || !isModuleEnabled("ConcurrentMap")) addBlockedFiles("ConcurrentMap", "ConcurrentOpenHashMap");
		
		boolean ordered = !isModuleEnabled("OrderedMap");
		if(ordered) addBlockedFiles("OrderedMap");
		boolean hashMap = implementations || !isModuleEnabled("HashMap");
		if(hashMap) addBlockedFiles("OpenHashMap");
		if(hashMap || ordered || !isModuleEnabled("LinkedHashMap")) addBlockedFiles("LinkedOpenHashMap");
		
		boolean customHashMap = implementations || !isModuleEnabled("CustomHashMap");
		if(customHashMap) addBlockedFiles("OpenCustomHashMap");
		if(customHashMap || ordered || !isModuleEnabled("LinkedCustomHashMap")) addBlockedFiles("LinkedOpenCustomHashMap");
		
		boolean enumMap = implementations || !isModuleEnabled("EnumMap");
		if(enumMap) addBlockedFiles("EnumMap");
		if(enumMap || ordered || !isModuleEnabled("LinkedEnumMap")) addBlockedFiles("LinkedEnumMap");
		
		if(ordered || !isModuleEnabled("ArrayMap")) addBlockedFiles("ArrayMap");
		
		boolean sorted = !isModuleEnabled("SortedMap");
		if(sorted) addBlockedFiles("SortedMap", "NavigableMap");
		if(implementations || sorted || !isModuleEnabled("AVLTreeMap")) addBlockedFiles("AVLTreeMap");
		if(implementations || sorted || !isModuleEnabled("RBTreeMap")) addBlockedFiles("RBTreeMap");
		
		if(keyType == ClassType.BOOLEAN)
		{
			//Main Classes
			addBlockedFiles("SortedMap", "NavigableMap", "RBTreeMap", "AVLTreeMap");
			addBlockedFiles("OrderedMap", "ArrayMap", "LinkedOpenHashMap", "LinkedOpenCustomHashMap");
			addBlockedFiles("ConcurrentMap", "ConcurrentOpenHashMap");
			addBlockedFiles("Map", "Maps", "AbstractMap", "ImmutableOpenHashMap", "OpenHashMap", "OpenCustomHashMap");
			
			//Test Classes
			addBlockedFiles("TestMap", "MapTests", "MapTestSuiteBuilder", "MapConstructorTests", "TestMapGenerator", "SimpleMapTestGenerator", "DerivedMapGenerators", "AbstractMapTester");
			addBlockedFiles("TestSortedMapGenerator", "OrderedMapTestSuiteBuilder", "NavigableMapTestSuiteBuilder", "SortedMapTestSuiteBuilder");
			addBlockedFiles("TestOrderedMapGenerator");
			addBlockedFilter(T -> T.endsWith("Tester") && (T.startsWith("Map") || T.startsWith("OrderedMap") || T.startsWith("SortedMap") || T.startsWith("NavigableMap")));
		}
	}
	
	@Override
	protected void loadRemappers()
	{
		//Main Classes
		addBiRequirement("Map");
		addBiRequirement("SortedMap");
		addBiRequirement("OrderedMap");
		addBiRequirement("NavigableMap");
		addBiRequirement("ConcurrentMap");
		addBiRequirement("AbstractMap");
		addEnumRequirement("EnumMap");
		addEnumRequirement("LinkedEnumMap");
		addBiRequirement("ConcurrentOpenHashMap");
		addBiRequirement("ImmutableOpenHashMap");
		addBiRequirement("OpenHashMap");
		addBiRequirement("LinkedOpenHashMap");
		addBiRequirement("OpenCustomHashMap");
		addBiRequirement("LinkedOpenCustomHashMap");
		addBiRequirement("ArrayMap");
		addBiRequirement("RBTreeMap");
		addBiRequirement("AVLTreeMap");
		addBiRequirement("Maps");
		
		addRemapper("AbstractMap", "Abstract%sMap");
		addRemapper("EnumMap", "Enum2%sMap");
		addRemapper("LinkedEnumMap", "LinkedEnum2%sMap");
		addRemapper("ImmutableOpenHashMap", "Immutable%sOpenHashMap");
		
		//Test Classes
		addBiRequirement("TestMapGenerator");
		addBiRequirement("TestSortedMapGenerator");
		addBiRequirement("TestOrderedMapGenerator");
		addBiRequirement("SimpleMapTestGenerator");
		addBiRequirement("DerivedMapGenerators");
		addBiRequirement("AbstractMapTester");
		addBiRequirement("MapTestSuiteBuilder");
		addBiRequirement("SortedMapTestSuiteBuilder");
		addBiRequirement("NavigableMapTestSuiteBuilder");
		addBiRequirement("OrderedMapTestSuiteBuilder");
		addBiRequirement("MapTests");
		addBiRequirement("MapConstructorTests");
		addBiRequirement("TestMap");
		addBiRequirement("MapAddToTester");
		addBiRequirement("MapSubFromTester");
		addBiRequirement("MapClearTester");
		addBiRequirement("MapComputeIfAbsentTester");
		addBiRequirement("MapComputeIfPresentTester");
		addBiRequirement("MapComputeTester");
		addBiRequirement("MapCopyTester");
		addBiRequirement("MapContainsTester");
		addBiRequirement("MapContainsKeyTester");
		addBiRequirement("MapContainsValueTester");
		addBiRequirement("MapCreatorTester");
		addBiRequirement("MapEntrySetTester");
		addBiRequirement("MapEqualsTester");
		addBiRequirement("MapForEachTester");
		addBiRequirement("MapGetOrDefaultTester");
		addBiRequirement("MapGetTester");
		addBiRequirement("MapHashCodeTester");
		addBiRequirement("MapIsEmptyTester");
		addBiRequirement("MapMergeTester");
		addBiRequirement("MapMergeBulkTester");
		addBiRequirement("MapPutAllArrayTester");
		addBiRequirement("MapPutAllTester");
		addBiRequirement("MapPutIfAbsentTester");
		addBiRequirement("MapPutTester");
		addBiRequirement("MapRemoveEntryTester");
		addBiRequirement("MapRemoveOrDefaultTester");
		addBiRequirement("MapRemoveTester");
		addBiRequirement("MapReplaceAllTester");
		addBiRequirement("MapReplaceEntryTester");
		addBiRequirement("MapReplaceTester");
		addBiRequirement("MapSizeTester");
		addBiRequirement("MapSupplyIfAbsentTester");
		addBiRequirement("MapToStringTester");
		addBiRequirement("NavigableMapNavigationTester");
		addBiRequirement("SortedMapNavigationTester");
		addBiRequirement("OrderedMapNavigationTester");
		addBiRequirement("OrderedMapMoveTester");
		addBiRequirement("MapConstructorTester");
		
		addRemapper("TestMapGenerator", "Test%sMapGenerator");
		addRemapper("TestSortedMapGenerator", "Test%sSortedMapGenerator");
		addRemapper("TestOrderedMapGenerator", "Test%sOrderedMapGenerator");
		addRemapper("SimpleMapTestGenerator", "Simple%sMapTestGenerator");
		addRemapper("DerivedMapGenerators", "Derived%sMapGenerators");
		addRemapper("AbstractMapTester", "Abstract%sMapTester");
		addRemapper("TestMap", "Test%sMap");
	}
	
	@Override
	protected void loadFunctions()
	{
		addFunctionValueMapper("BULK_MERGE", "mergeAll");
		addFunctionValueMappers("COMPUTE_IF_ABSENT", "compute%sIfAbsent");
		addFunctionValueMappers("COMPUTE_IF_PRESENT", "compute%sIfPresent");
		addFunctionValueMapper("COMPUTE", "compute");
		addFunctionMapper("DEQUEUE_LAST", "dequeueLast");
		addFunctionMapper("DEQUEUE", "dequeue");
		addSimpleMapper("ENTRY_SET", keyType.getFileType().toLowerCase()+"2"+valueType.getFileType()+"EntrySet");
		addFunctionMappers("FIRST_ENTRY_KEY", "first%sKey");
		addFunctionValueMappers("FIRST_ENTRY_VALUE", "first%sValue");
		if(keyType.isObject()) addFunctionValueMapper("GET_VALUE", valueType.isObject() ? "getObject" : "get");
		else addSimpleMapper("GET_VALUE", "get");
		addFunctionMappers("LAST_ENTRY_KEY", "last%sKey");
		addFunctionValueMappers("LAST_ENTRY_VALUE", "last%sValue");
		addFunctionValueMapper("MERGE", "merge");
		addFunctionMappers("POLL_FIRST_ENTRY_KEY", "pollFirst%sKey");
		addFunctionMappers("POLL_LAST_ENTRY_KEY", "pollLast%sKey");
		if(keyType.isObject()) addFunctionMapper("REMOVE_VALUE", "rem");
		else addSimpleMapper("REMOVE_VALUE", "remove");
		addFunctionMapper("REMOVE", "remove");
		addFunctionValueMappers("REPLACE_VALUES", valueType.isObject() ? "replaceObjects" : "replace%ss");
		addFunctionValueMappers("SUPPLY_IF_ABSENT", "supply%sIfAbsent");
	}
	
	@Override
	protected void loadClasses()
	{
		//Implementation Classes
		addAbstractBiMapper("IMMUTABLE_HASH_MAP", "Immutable%sOpenHashMap", "2");
		addBiClassMapper("LINKED_CUSTOM_HASH_MAP", "LinkedOpenCustomHashMap", "2");
		addBiClassMapper("LINKED_HASH_MAP", "LinkedOpenHashMap", "2");
		addBiClassMapper("CUSTOM_HASH_MAP", "OpenCustomHashMap", "2");
		addBiClassMapper("CONCURRENT_HASH_MAP", "ConcurrentOpenHashMap", "2");
		addBiClassMapper("AVL_TREE_MAP", "AVLTreeMap", "2");
		addBiClassMapper("RB_TREE_MAP", "RBTreeMap", "2");
		addFunctionValueMappers("LINKED_ENUM_MAP", valueType.isObject() ? "LinkedEnum2ObjectMap" : "LinkedEnum2%sMap");
		addFunctionValueMappers("ENUM_MAP", valueType.isObject() ? "Enum2ObjectMap" : "Enum2%sMap");
		addBiClassMapper("HASH_MAP", "OpenHashMap", "2");
		addBiClassMapper("ARRAY_MAP", "ArrayMap", "2");
		
		//Abstract Classes
		addAbstractBiMapper("ABSTRACT_MAP", "Abstract%sMap", "2");
		
		//Helper Classes
		addBiClassMapper("MAPS", "Maps", "2");
		
		//Interfaces
		addBiClassMapper("NAVIGABLE_MAP", "NavigableMap", "2");
		addBiClassMapper("ORDERED_MAP", "OrderedMap", "2");
		addBiClassMapper("SORTED_MAP", "SortedMap", "2");
		addBiClassMapper("CONCURRENT_MAP", "ConcurrentMap", "2");
		addBiClassMapper("MAP", "Map", "2");
	}
	
	@Override
	protected void loadTestClasses()
	{
		//Implementation Classes
		addAbstractBiMapper("SIMPLE_TEST_MAP", "Test%sMap", "2");
		addBiClassMapper("MAP_TESTS", "MapTests", "2");
		addAbstractBiMapper("NAVIGABLE_MAP_TEST_BUILDER", "%sNavigableMapTestSuiteBuilder", "2");
		addAbstractBiMapper("SORTED_MAP_TEST_BUILDER", "%sSortedMapTestSuiteBuilder", "2");
		addAbstractBiMapper("ORDERED_MAP_TEST_BUILDER", "%sOrderedMapTestSuiteBuilder", "2");
		addAbstractBiMapper("MAP_TEST_BUILDER", "%sMapTestSuiteBuilder", "2");
		
		//Abstract Classes
		addAbstractBiMapper("ABSTRACT_MAP_TESTER", "Abstract%sMapTester", "2");
		
		//Helper Classes
		addAbstractBiMapper("MAP_CONSTRUCTOR_TESTS", "%sMapConstructorTests", "2");
		addAbstractBiMapper("SIMPLE_MAP_TEST_GENERATOR", "Simple%sMapTestGenerator", "2");
		addAbstractBiMapper("DERIVED_MAP_GENERATORS", "Derived%sMapGenerators", "2");
		addAbstractBiMapper("TEST_ORDERED_MAP_GENERATOR", "Test%sOrderedMapGenerator", "2");
		addAbstractBiMapper("TEST_SORTED_MAP_GENERATOR", "Test%sSortedMapGenerator", "2");
		addAbstractBiMapper("TEST_MAP_GENERATOR", "Test%sMapGenerator", "2");
	}
}