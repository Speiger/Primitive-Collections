package speiger.src.builder.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependencies.FunctionDependency;
import speiger.src.builder.dependencies.IDependency;
import speiger.src.builder.dependencies.ModuleDependency;

@SuppressWarnings("javadoc")
public class MapModule extends BaseModule
{
	public static final BaseModule INSTANCE = new MapModule();
	public static final ModuleDependency MODULE = new ModuleDependency(INSTANCE, true)
			.addKeyDependency(SetModule.MODULE)
			.addValueDependency(CollectionModule.MODULE)
			.addEntryDependency(SetModule.MODULE)
			.addTypeDependency(SetModule.MODULE, ClassType.OBJECT);
	public static final FunctionDependency IMPLEMENTATION = MODULE.createDependency("Implementations");
	public static final FunctionDependency WRAPPERS = MODULE.createDependency("Wrappers").addKeyDependency(SetModule.WRAPPERS).addOptionalTypeDependency(SetModule.WRAPPERS, ClassType.OBJECT, true);

	public static final FunctionDependency ORDERED_MAP = MODULE.createDependency("OrderedMap").addKeyDependency(SetModule.ORDERED_SET).addOptionalTypeDependency(SetModule.ORDERED_SET, ClassType.OBJECT, true);
	public static final FunctionDependency SORTED_MAP = MODULE.createDependency("SortedMap").addKeyDependency(SetModule.SORTED_SET).addOptionalTypeDependency(SetModule.SORTED_SET, ClassType.OBJECT, true);

	public static final FunctionDependency ARRAY_MAP = MODULE.createDependency("ArrayMap").addEntryDependency(ORDERED_MAP).addEntryDependency(IMPLEMENTATION);
	public static final FunctionDependency IMMUTABLE_MAP = MODULE.createDependency("ImmutableMap").addEntryDependency(IMPLEMENTATION);

	public static final FunctionDependency HASH_MAP = MODULE.createDependency("HashMap").addEntryDependency(IMPLEMENTATION);
	public static final FunctionDependency LINKED_MAP = MODULE.createDependency("LinkedHashMap").addEntryDependency(HASH_MAP).addEntryDependency(ORDERED_MAP);
	
	public static final FunctionDependency CUSTOM_MAP = MODULE.createDependency("CustomHashMap").addEntryDependency(IMPLEMENTATION).addKeyDependency(CollectionModule.STRATEGY);
	public static final FunctionDependency LINKED_CUSTOM_MAP = MODULE.createDependency("LinkedCustomHashMap").addEntryDependency(CUSTOM_MAP).addEntryDependency(ORDERED_MAP);

	public static final FunctionDependency ENUM_MAP = MODULE.createDependency("EnumMap").addEntryDependency(IMPLEMENTATION);
	public static final FunctionDependency LINKED_ENUM_MAP = MODULE.createDependency("LinkedEnumMap").addEntryDependency(ENUM_MAP).addEntryDependency(ORDERED_MAP);
	
	public static final FunctionDependency CONCURRENT_MAP = MODULE.createDependency("ConcurrentMap").addEntryDependency(IMPLEMENTATION);
	public static final FunctionDependency AVL_TREE_MAP = MODULE.createDependency("AVLTreeMap").addEntryDependency(SORTED_MAP).addEntryDependency(IMPLEMENTATION);
	public static final FunctionDependency RB_TREE_MAP = MODULE.createDependency("RBTreeMap").addEntryDependency(SORTED_MAP).addEntryDependency(IMPLEMENTATION);
	
	@Override
	public String getModuleName() { return "Map"; }
	@Override
	public boolean isBiModule() { return true; }
	@Override
	protected void loadVariables() {}
	@Override
	public boolean isModuleValid(ClassType keyType, ClassType valueType) { return keyType != ClassType.BOOLEAN; }
	@Override
	public List<IDependency> getDependencies(ClassType keyType, ClassType valueType) {
		List<IDependency> dependencies = new ArrayList<>(Arrays.asList(MODULE, ORDERED_MAP, SORTED_MAP, IMPLEMENTATION, WRAPPERS, ARRAY_MAP, IMMUTABLE_MAP, HASH_MAP, LINKED_MAP, CUSTOM_MAP, LINKED_CUSTOM_MAP, CONCURRENT_MAP, AVL_TREE_MAP, RB_TREE_MAP));
		if(keyType == ClassType.OBJECT) dependencies.addAll(Arrays.asList(ENUM_MAP, LINKED_ENUM_MAP));
		return dependencies;
	}
	
	@Override
	protected void loadFlags()
	{
		if(MODULE.isEnabled()) addFlag("MAP_MODULE");
		if(WRAPPERS.isEnabled()) addFlag("MAPS_FEATURE");
		if(ORDERED_MAP.isEnabled()) addFlag("ORDERED_MAP_FEATURE");
		if(ARRAY_MAP.isEnabled()) addFlag("ARRAY_MAP_FEATURE");
		if(LINKED_MAP.isEnabled()) addFlag("LINKED_MAP_FEATURE");
		if(LINKED_CUSTOM_MAP.isEnabled()) addFlag("LINKED_CUSTOM_MAP_FEATURE");
		if(LINKED_ENUM_MAP.isEnabled()) addFlag("LINKED_ENUM_MAP_FEATURE");
		
		if(SORTED_MAP.isEnabled()) addFlag("SORTED_MAP_FEATURE");
		if(AVL_TREE_MAP.isEnabled()) addFlag("AVL_TREE_MAP_FEATURE");
		if(RB_TREE_MAP.isEnabled()) addFlag("RB_TREE_MAP_FEATURE");
		
		if(CONCURRENT_MAP.isEnabled()) addFlag("CONCURRENT_MAP_FEATURE");
		if(IMMUTABLE_MAP.isEnabled()) addFlag("IMMUTABLE_MAP_FEATURE");
		if(HASH_MAP.isEnabled()) addFlag("MAP_FEATURE");
		if(CUSTOM_MAP.isEnabled()) addFlag("CUSTOM_MAP_FEATURE");
		if(ENUM_MAP.isEnabled()) addFlag("ENUM_MAP_FEATURE");
	}
	
	@Override
	protected void loadBlockades()
	{
		if(!MODULE.isEnabled()) addBlockedFiles("Map", "AbstractMap");
		if(!WRAPPERS.isEnabled()) addBlockedFiles("Maps");
		if(!IMMUTABLE_MAP.isEnabled()) addBlockedFiles("ImmutableOpenHashMap");
		if(!CONCURRENT_MAP.isEnabled()) addBlockedFiles("ConcurrentMap", "ConcurrentOpenHashMap");
		if(!ORDERED_MAP.isEnabled()) addBlockedFiles("OrderedMap");
		if(!HASH_MAP.isEnabled()) addBlockedFiles("OpenHashMap");
		if(!LINKED_MAP.isEnabled()) addBlockedFiles("LinkedOpenHashMap");
		if(!CUSTOM_MAP.isEnabled()) addBlockedFiles("OpenCustomHashMap");
		if(!LINKED_CUSTOM_MAP.isEnabled()) addBlockedFiles("LinkedOpenCustomHashMap");
		if(!ENUM_MAP.isEnabled()) addBlockedFiles("EnumMap");
		if(!LINKED_ENUM_MAP.isEnabled()) addBlockedFiles("LinkedEnumMap");
		if(!ARRAY_MAP.isEnabled()) addBlockedFiles("ArrayMap");
		if(!SORTED_MAP.isEnabled()) addBlockedFiles("SortedMap", "NavigableMap");
		if(!AVL_TREE_MAP.isEnabled()) addBlockedFiles("AVLTreeMap");
		if(!RB_TREE_MAP.isEnabled()) addBlockedFiles("RBTreeMap");
		
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
		if(valueType == ClassType.OBJECT) {
			addBlockedFiles("MapComputeIfAbsentNonDefaultTester", "MapComputeIfPresentNonDefaultTester", "MapComputeNonDefaultTester", "MapSupplyIfAbsentNonDefaultTester");
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
		addBiRequirement("MapComputeIfAbsentNonDefaultTester");
		addBiRequirement("MapComputeIfPresentNonDefaultTester");
		addBiRequirement("MapComputeNonDefaultTester");
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
		addBiRequirement("MapSupplyIfAbsentNonDefaultTester");
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