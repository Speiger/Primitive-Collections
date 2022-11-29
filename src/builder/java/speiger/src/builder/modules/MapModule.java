package speiger.src.builder.modules;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class MapModule extends BaseModule
{
	@Override
	protected void loadVariables()
	{
		loadFunctions();
		loadClasses();
		loadRemappers();
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
			addBlockedFiles("SortedMap", "NavigableMap", "RBTreeMap", "AVLTreeMap");
			addBlockedFiles("OrderedMap", "ArrayMap", "LinkedOpenHashMap", "LinkedOpenCustomHashMap");
			addBlockedFiles("ConcurrentMap", "ConcurrentOpenHashMap");
			addBlockedFiles("ImmutableOpenHashMap", "OpenHashMap", "OpenCustomHashMap");
		}
	}
	
	private void loadRemappers()
	{
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
	}
	
	private void loadFunctions()
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
		addSimpleMapper("GET_JAVA", keyType.isObject() ? "get" : "getAs"+keyType.getCustomJDKType().getNonFileType());
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
	
	private void loadClasses()
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
}