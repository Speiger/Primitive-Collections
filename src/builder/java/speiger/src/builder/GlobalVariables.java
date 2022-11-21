package speiger.src.builder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import speiger.src.builder.mappers.ArgumentMapper;
import speiger.src.builder.mappers.IMapper;
import speiger.src.builder.mappers.InjectMapper;
import speiger.src.builder.mappers.LineMapper;
import speiger.src.builder.mappers.SimpleMapper;
import speiger.src.builder.processor.TemplateProcess;

@SuppressWarnings("javadoc")
public class GlobalVariables
{
	List<IMapper> operators = new ArrayList<>();
	Set<String> flags = new LinkedHashSet<>();
	ClassType type;
	ClassType valueType;
	
	public GlobalVariables(ClassType type, ClassType subType)
	{
		this.type = type;
		valueType = subType;
	}
	
	public GlobalVariables createVariables()
	{
		addSimpleMapper("VALUE_PACKAGE", valueType.getPathType());
		addSimpleMapper("PACKAGE", type.getPathType());
		addSimpleMapper("CLASS_TYPE", type.getClassType());
		addSimpleMapper("CLASS_VALUE_TYPE", valueType.getClassValueType());
		addSimpleMapper("KEY_TYPE", type.getKeyType());
		addSimpleMapper("KEY_OBJECT_TYPE", type.isObject() ? "Object" : type.getKeyType());
		addSimpleMapper("KEY_STRING_TYPE", type.isObject() ? "String" : type.getKeyType());
		addSimpleMapper("KEY_SPECIAL_TYPE", type.isObject() ? "E" : type.getKeyType());
		addSimpleMapper("CLASS_OBJECT_TYPE", type.getClassType());
		addSimpleMapper("CLASS_OBJECT_VALUE_TYPE", valueType.getClassValueType());
		addSimpleMapper("CLASS_STRING_TYPE", type.isObject() ? "String" : type.getClassType());
		addSimpleMapper("CLASS_STRING_VALUE_TYPE", valueType.isObject() ? "String" : valueType.getClassValueType());
		addSimpleMapper("VALUE_TYPE", valueType.getValueType());
		addSimpleMapper("VALUE_OBJECT_TYPE", valueType.isObject() ? "Object" : valueType.getValueType());
		addSimpleMapper("VALUE_STRING_TYPE", valueType.isObject() ? "String" : valueType.getValueType());
		addSimpleMapper("VALUE_SPECIAL_TYPE", valueType.isObject() ? "E" : valueType.getKeyType());
		addSimpleMapper("KEY_JAVA_TYPE", type.getCustomJDKType().getKeyType());
		addSimpleMapper("VALUE_JAVA_TYPE", type.getCustomJDKType().getKeyType());
		
		addSimpleMapper("EMPTY_KEY_VALUE", type.getEmptyValue());
		addSimpleMapper("EMPTY_VALUE", valueType.getEmptyValue());
		
		addSimpleMapper("INVALID_KEY_VALUE", type.getInvalidValue());
		addSimpleMapper("INVALID_VALUE", valueType.getInvalidValue());
		
		addSimpleMapper(" KEY_STRING_GENERIC_TYPE", type.isObject() ? "<String>" : "");
		addSimpleMapper(" VALUE_STRING_GENERIC_TYPE", valueType.isObject() ? "<String>" : "");
		addSimpleMapper(" KEY_VALUE_STRING_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<String, String>" : "<String>") : (valueType.isObject() ? "<String>" : ""));

		
		addSimpleMapper(" KEY_GENERIC_TYPE", type.isObject() ? "<"+type.getKeyType()+">" : "");
		addSimpleMapper(" KEY_KEY_GENERIC_TYPE", type.isObject() ? "<"+type.getKeyType()+", "+type.getKeyType()+">" : "");
		addSimpleMapper(" KEY_CLASS_GENERIC_TYPE", type.isObject() ? "<"+type.getClassType()+">" : "");

		
		addSimpleMapper(" VALUE_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+">" : "");
		addSimpleMapper(" VALUE_VALUE_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+", "+valueType.getValueType()+">" : "");
		addSimpleMapper(" VALUE_CLASS_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getClassValueType()+">" : "");
		
		addSimpleMapper(" KEY_VALUE_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+", "+valueType.getValueType()+">" : "<"+type.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" KEY_VALUE_VALUE_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+", "+valueType.getValueType()+", "+valueType.getValueType()+">" : "<"+type.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+", "+valueType.getValueType()+">" : ""));
		addInjectMapper(" KEY_VALUE_SPECIAL_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+", "+valueType.getValueType()+", %s>" : "<"+type.getKeyType()+", %s>") : (valueType.isObject() ? "<"+valueType.getValueType()+", %s>" : "<%s>")).setBraceType("<>").removeBraces();
		
		addSimpleMapper(" NO_GENERIC_TYPE", type.isObject() ? "<?>" : "");
		addSimpleMapper(" NO_KV_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<?, ?>" : "<?>") : valueType.isObject() ? "<?>" : "");
		addSimpleMapper(" KEY_COMPAREABLE_TYPE", type.isObject() ? "<"+type.getKeyType()+" extends Comparable<T>>" : "");
		
		addSimpleMapper(" KEY_SUPER_GENERIC_TYPE", type.isObject() ? "<? super "+type.getKeyType()+">" : "");
		addSimpleMapper(" VALUE_SUPER_GENERIC_TYPE", valueType.isObject() ? "<? super "+valueType.getValueType()+">" : "");
		addSimpleMapper(" KEY_VALUE_SUPER_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<? super "+type.getKeyType()+", ? super "+valueType.getValueType()+">" : "<? super "+type.getKeyType()+">") : (valueType.isObject() ? "<? super "+valueType.getValueType()+">" : ""));
		
		addSimpleMapper(" KEY_UNKNOWN_GENERIC_TYPE", type.isObject() ? "<? extends "+type.getKeyType()+">" : "");
		addSimpleMapper(" VALUE_UNKNOWN_GENERIC_TYPE", valueType.isObject() ? "<? extends "+valueType.getValueType()+">" : "");
		addSimpleMapper(" KEY_VALUE_UNKNOWN_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<? extends "+type.getKeyType()+", ? extends "+valueType.getValueType()+">" : "<? extends "+type.getKeyType()+">") : (valueType.isObject() ? "<? extends "+valueType.getValueType()+">" : ""));

		addSimpleMapper(" KEY_ENUM_VALUE_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+" extends Enum<"+type.getKeyType()+">, "+valueType.getValueType()+">" : "<"+type.getKeyType()+" extends Enum<"+type.getKeyType()+">>") : (valueType.isObject() ? "<"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" KEY_VALUE_ENUM_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+", "+valueType.getValueType()+" extends Enum<"+valueType.getValueType()+">>" : "<"+type.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+" extends Enum<"+valueType.getValueType()+">>" : ""));
		
		addInjectMapper(" KEY_SPECIAL_GENERIC_TYPE", type.isObject() ? "<%s>" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" VALUE_SPECIAL_GENERIC_TYPE", valueType.isObject() ? "<%s>" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" KSK_GENERIC_TYPE", type.isObject() ? "<%s, "+type.getKeyType()+">" : "<%s>").removeBraces().setBraceType("<>");
		addInjectMapper(" KKS_GENERIC_TYPE", type.isObject() ? "<"+type.getKeyType()+", %s>" : "<%s>").removeBraces().setBraceType("<>");
		addArgumentMapper(" KSS_GENERIC_TYPE", type.isObject() ? "<%1$s, %2$s>" : "<%2$s>").removeBraces().setBraceType("<>");		
		addInjectMapper(" VSV_GENERIC_TYPE", valueType.isObject() ? "<%s, "+valueType.getValueType()+">" : "<%s>").removeBraces().setBraceType("<>");
		addInjectMapper(" VVS_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+", %s>" : "<%s>").removeBraces().setBraceType("<>");
		addArgumentMapper(" VSS_GENERIC_TYPE", valueType.isObject() ? "<%1$s, %2$s>" : "<%2$s>").removeBraces().setBraceType("<>");		

		addSimpleMapper(" GENERIC_KEY_BRACES", type.isObject() ? " <"+type.getKeyType()+">" : "");
		addSimpleMapper(" GENERIC_VALUE_BRACES", valueType.isObject() ? " <"+valueType.getValueType()+">" : "");
		addInjectMapper(" GENERIC_SPECIAL_KEY_BRACES", type.isObject() ? " <%s>" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" GENERIC_SPECIAL_VALUE_BRACES", valueType.isObject() ? " <%s>" : "").removeBraces().setBraceType("<>");
		addSimpleMapper(" GENERIC_KEY_ENUM_VALUE_BRACES", type.isObject() ? (valueType.isObject() ? " <"+type.getKeyType()+" extends Enum<"+type.getKeyType()+">, "+valueType.getValueType()+">" : " <"+type.getKeyType()+" extends Enum<"+type.getKeyType()+">>") : (valueType.isObject() ? " <"+valueType.getValueType()+">" : ""));
		
		addInjectMapper(" GENERIC_KEY_SPECIAL_BRACES", type.isObject() ? " <"+type.getKeyType()+", %s>" : " <%s>").removeBraces().setBraceType("<>");
		addInjectMapper(" GENERIC_VALUE_SPECIAL_BRACES", valueType.isObject() ? " <"+valueType.getKeyType()+", %s>" : " <%s>").removeBraces().setBraceType("<>");
		
		addSimpleMapper(" GENERIC_KEY_VALUE_BRACES", type.isObject() ? (valueType.isObject() ? " <"+type.getKeyType()+", "+valueType.getValueType()+">" : " <"+type.getKeyType()+">") : (valueType.isObject() ? " <"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" COMPAREABLE_KEY_BRACES", type.isObject() ? " <"+type.getKeyType()+" extends Comparable<T>>" : "");
		addSimpleMapper("KV_BRACES", type.isObject() || valueType.isObject() ? "<>" : "");
		addSimpleMapper("VALUE_BRACES", valueType.isObject() ? "<>" : "");
		addSimpleMapper("BRACES", type.isObject() ? "<>" : "");
		if(type.needsCustomJDKType())
		{
			addSimpleMapper("JAVA_TYPE", type.getCustomJDKType().getKeyType());
			addSimpleMapper("SANITY_CAST", "castTo"+type.getFileType());
		}
		addSimpleMapper("JAVA_CLASS", type.getCustomJDKType().getClassType());
		if(valueType.needsCustomJDKType())
		{
			addSimpleMapper("SANITY_CAST_VALUE", "castTo"+valueType.getFileType());
		}
		addComment("@ArrayType", "@param <%s> the type of array that the operation should be applied");
		addComment("@Type", "@param <%s> the type of elements maintained by this Collection");
		addValueComment("@ValueArrayType", "@param <%s> the type of array that the operation should be applied");
		addValueComment("@ValueType", "@param <%s> the type of elements maintained by this Collection");
		addAnnontion("@PrimitiveOverride", "@Override");
		addSimpleMapper("@PrimitiveDoc", "");
		addAnnontion("@Primitive", "@Deprecated");
		addValueAnnontion("@ValuePrimitiveOverride", "@Override");
		addValueAnnontion("@ValuePrimitive", "@Deprecated");
		return this;
	}
	
	public GlobalVariables createHelperVariables()
	{
		createHelperVars(type, false, "KEY");
		createHelperVars(valueType, true, "VALUE");
		return this;
	}
	
	private void createHelperVars(ClassType type, boolean value, String fix)
	{
		addArgumentMapper("EQUALS_"+fix+"_TYPE", "Objects.equals(%2$s, "+(type.isObject() ? "%1$s" : fix+"_TO_OBJ(%1$s)")+")").removeBraces();
		addInjectMapper(fix+"_EQUALS_NOT_NULL", type.getComparableValue()+" != "+(type.isPrimitiveBlocking() || type.needsCast() ? type.getEmptyValue() : "0")).removeBraces();
		addInjectMapper(fix+"_EQUALS_NULL", type.getComparableValue()+" == "+(type.isPrimitiveBlocking() || type.needsCast() ? type.getEmptyValue() : "0")).removeBraces();
		addArgumentMapper(fix+"_EQUALS_NOT", type.getEquals(true)).removeBraces();
		addArgumentMapper(fix+"_EQUALS", type.getEquals(false)).removeBraces();
		addSimpleMapper("FILE_"+fix+"_TYPE", type.getFileType());
		
		addArgumentMapper("COMPAREABLE_TO_"+fix, type.isObject() ? "((Comparable<"+type.getKeyType(value)+">)%1$s).compareTo(("+type.getKeyType(value)+")%2$s)" : type.getClassType(value)+".compare(%1$s, %2$s)").removeBraces();
		addArgumentMapper("COMPARE_TO_"+fix, type.isObject() ? "%1$s.compareTo(%2$s)" : type.getClassType(value)+".compare(%1$s, %2$s)").removeBraces();
		
		addInjectMapper(fix+"_TO_OBJ", type.isObject() ? "%s" : type.getClassType(value)+".valueOf(%s)").removeBraces();
		addInjectMapper("OBJ_TO_"+fix, type.isObject() ? "%s" : "%s."+type.getKeyType(value)+"Value()").removeBraces();
		addInjectMapper("CLASS_TO_"+fix, type.isObject() ? "("+type.getKeyType(value)+")%s" : "(("+type.getClassType(value)+")%s)."+type.getKeyType(value)+"Value()").removeBraces();
		
		addInjectMapper(fix+"_TO_HASH", type.isObject() ? "Objects.hashCode(%s)" : type.getClassType(value)+".hashCode(%s)").removeBraces();
		addInjectMapper(fix+"_TO_STRING", type.isObject() ? "Objects.toString(%s)" : type.getClassType(value)+".toString(%s)").removeBraces();
		
		addSimpleMapper("CAST_"+fix+"_ARRAY ", type.isObject() ? "("+fix+"_TYPE[])" : "");
		addSimpleMapper("EMPTY_"+fix+"_ARRAY", type.isObject() ? "("+fix+"_TYPE[])ARRAYS.EMPTY_ARRAY" : "ARRAYS.EMPTY_ARRAY");
		addInjectMapper("NEW_"+fix+"_ARRAY", type.isObject() ? "("+fix+"_TYPE[])new Object[%s]" : "new "+fix+"_TYPE[%s]").removeBraces();
		addInjectMapper("NEW_SPECIAL_"+fix+"_ARRAY", type.isObject() ? "(E[])new Object[%s]" : "new "+fix+"_TYPE[%s]").removeBraces();
		if(value) addInjectMapper("NEW_CLASS_VALUE_ARRAY", type.isObject() ? "(CLASS_VALUE_TYPE[])new Object[%s]" : "new CLASS_VALUE_TYPE[%s]").removeBraces();
		else addInjectMapper("NEW_CLASS_ARRAY", type.isObject() ? "(CLASS_TYPE[])new Object[%s]" : "new CLASS_TYPE[%s]").removeBraces();
	}
	
	public GlobalVariables createPreFunctions()
	{
		addSimpleMapper("ENTRY_SET", type.getFileType().toLowerCase()+"2"+valueType.getFileType()+"EntrySet");
		return this;
	}
	
	public GlobalVariables createClassTypes()
	{
		addSimpleMapper("JAVA_PREDICATE", type.isPrimitiveBlocking() ? "" : type.getCustomJDKType().getFileType()+"Predicate");
		addSimpleMapper("JAVA_CONSUMER", type.isPrimitiveBlocking() ? "" : "java.util.function."+type.getCustomJDKType().getFileType()+"Consumer");
		addSimpleMapper("JAVA_SUPPLIER", type.isPrimitiveBlocking() ? "" : "java.util.function."+type.getCustomJDKType().getFileType()+"Supplier");
		addSimpleMapper("JAVA_FUNCTION", type.getFunctionClass(valueType));
		addSimpleMapper("JAVA_BINARY_OPERATOR", type == ClassType.BOOLEAN ? "" : (type.isObject() ? "java.util.function.BinaryOperator" : "java.util.function."+type.getCustomJDKType().getFileType()+"BinaryOperator"));
		addSimpleMapper("JAVA_UNARY_OPERATOR", type.isObject() ? "BinaryOperator" : type == ClassType.BOOLEAN ? "" : type.getCustomJDKType().getFileType()+"UnaryOperator");
		addSimpleMapper("JAVA_SPLIT_ITERATOR", type.isPrimitiveBlocking() ? "Spliterator" : "Of"+type.getCustomJDKType().getFileType());
		addSimpleMapper("JAVA_STREAM", type.isPrimitiveBlocking() ? "" : type.getCustomJDKType().getFileType()+"Stream");
		addSimpleMapper("JAVA_BUFFER", type.getFileType()+"Buffer");
		
		//Final Classes
		addClassMapper("ARRAY_LIST", "ArrayList");
		addAbstractMapper("COPY_ON_WRITE_LIST", "CopyOnWrite%sArrayList");
		addClassMapper("ASYNC_BUILDER", "AsyncBuilder");
		addClassMapper("LINKED_LIST", "LinkedList");
		addAbstractMapper("IMMUTABLE_LIST", "Immutable%sList");
		addClassMapper("ARRAY_FIFO_QUEUE", "ArrayFIFOQueue");
		addClassMapper("ARRAY_PRIORITY_QUEUE", "ArrayPriorityQueue");
		addClassMapper("HEAP_PRIORITY_QUEUE", "HeapPriorityQueue");
		addClassMapper("LINKED_CUSTOM_HASH_SET", "LinkedOpenCustomHashSet");
		addClassMapper("LINKED_HASH_SET", "LinkedOpenHashSet");
		addAbstractMapper("IMMUTABLE_HASH_SET", "Immutable%sOpenHashSet");
		addClassMapper("CUSTOM_HASH_SET", "OpenCustomHashSet");
		addClassMapper("HASH_SET", "OpenHashSet");
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
		addBiClassMapper("IMMUTABLE_PAIR", "ImmutablePair", "");
		addBiClassMapper("MUTABLE_PAIR", "MutablePair", "");
		addClassMapper("RB_TREE_SET", "RBTreeSet");
		addClassMapper("AVL_TREE_SET", "AVLTreeSet");
		addClassMapper("ARRAY_SET", "ArraySet");
		addAbstractBiMapper("SIMPLE_TEST_MAP", "Test%sMap", "2");
		
		//Final UnitTest Classes
		addAbstractMapper("MINIMAL_COLLECTION", "Minimal%sCollection");
		addAbstractMapper("MINIMAL_SET", "Minimal%sSet");
		addAbstractMapper("ABSTRACT_CONTAINER_TESTER", "Abstract%sContainerTester");
		addAbstractMapper("ABSTRACT_COLLECTION_TESTER", "Abstract%sCollectionTester");
		addAbstractMapper("ABSTRACT_QUEUE_TESTER", "Abstract%sQueueTester");
		addAbstractMapper("ABSTRACT_LIST_INDEX_OF_TESTER", "Abstract%sListIndexOfTester");
		addAbstractMapper("ABSTRACT_LIST_TESTER", "Abstract%sListTester");
		addAbstractMapper("ABSTRACT_SET_TESTER", "Abstract%sSetTester");
		addAbstractMapper("ABSTRACT_ITERATOR_TESTER", "Abstract%sIteratorTester");
		addAbstractBiMapper("ABSTRACT_MAP_TESTER", "Abstract%sMapTester", "2");
		addClassMapper("LIST_ITERATOR_TESTER", "ListIteratorTester");
		addClassMapper("BIDIRECTIONAL_ITERATOR_TESTER", "BidirectionalteratorTester");
		addClassMapper("ITERATOR_TESTER", "IteratorTester");
		addClassMapper("COLLECTION_TEST_BUILDER", "CollectionTestSuiteBuilder");
		addClassMapper("DEQUEUE_TEST_BUILDER", "DequeueTestSuiteBuilder");
		addClassMapper("QUEUE_TEST_BUILDER", "QueueTestSuiteBuilder");
		addClassMapper("LIST_TEST_BUILDER", "ListTestSuiteBuilder");
		addClassMapper("ORDERED_SET_TEST_BUILDER", "OrderedSetTestSuiteBuilder");
		addClassMapper("SORTED_SET_TEST_BUILDER", "SortedSetTestSuiteBuilder");
		addClassMapper("NAVIGABLE_SET_TEST_BUILDER", "NavigableSetTestSuiteBuilder");
		addClassMapper("SET_TEST_BUILDER", "SetTestSuiteBuilder");
		addAbstractBiMapper("NAVIGABLE_MAP_TEST_BUILDER", "%sNavigableMapTestSuiteBuilder", "2");
		addAbstractBiMapper("SORTED_MAP_TEST_BUILDER", "%sSortedMapTestSuiteBuilder", "2");
		addAbstractBiMapper("ORDERED_MAP_TEST_BUILDER", "%sOrderedMapTestSuiteBuilder", "2");
		addAbstractBiMapper("MAP_TEST_BUILDER", "%sMapTestSuiteBuilder", "2");
		addAbstractBiMapper("MAP_CONSTRUCTOR_TESTS", "%sMapConstructorTests", "2");
		addClassMapper("COLLECTION_CONSTRUCTOR_TESTS", "CollectionConstructorTests");
		addClassMapper("SUB_SORTED_SET_CLASS_GENERATOR", "SortedSetSubsetTestSetGenerator");
		addClassMapper("SUB_NAVIGABLE_SET_CLASS_GENERATOR", "NavigableSetSubsetTestSetGenerator");
		addClassMapper("LIST_TESTS", "ListTests");
		addClassMapper("SET_TESTS", "SetTests");
		addClassMapper("QUEUE_TESTS", "QueueTests");
		addBiClassMapper("MAP_TESTS", "MapTests", "2");

		//Abstract Classes
		addAbstractMapper("ABSTRACT_COLLECTION", "Abstract%sCollection");
		addAbstractMapper("ABSTRACT_PRIORITY_QUEUE", "Abstract%sPriorityQueue");
		addAbstractMapper("ABSTRACT_SET", "Abstract%sSet");
		addAbstractMapper("ABSTRACT_LIST", "Abstract%sList");
		addAbstractBiMapper("ABSTRACT_MAP", "Abstract%sMap", "2");
		addClassMapper("SUB_LIST", "SubList");
		addAbstractMapper("BASE_TASK", "Base%sTask");
		
		//Helper Classes
		addClassMapper("LISTS", "Lists");
		addClassMapper("SETS", "Sets");
		addClassMapper("COLLECTIONS", "Collections");
		addClassMapper("ARRAYS", "Arrays");
		addClassMapper("PRIORITY_QUEUES", "PriorityQueues");
		addClassMapper("SPLIT_ITERATORS", "Splititerators");
		addClassMapper("ITERATORS", "Iterators");
		addClassMapper("ITERABLES", "Iterables");
		addBiClassMapper("MAPS", "Maps", "2");
		addClassMapper("SAMPLE_ELEMENTS", "Samples");
		
		//UnitTest Helper Classes
		addClassMapper("HELPERS", "Helpers");
		addAbstractMapper("TEST_COLLECTION_GENERATOR", "Test%sCollectionGenerator");
		addAbstractMapper("TEST_QUEUE_GENERATOR", "Test%sQueueGenerator");
		addAbstractMapper("TEST_LIST_GENERATOR", "Test%sListGenerator");
		addAbstractMapper("TEST_NAVIGABLE_SET_GENERATOR", "Test%sNavigableSetGenerator");
		addAbstractMapper("TEST_SORTED_SET_GENERATOR", "Test%sSortedSetGenerator");
		addAbstractMapper("TEST_ORDERED_SET_GENERATOR", "Test%sOrderedSetGenerator");
		addAbstractMapper("TEST_SET_GENERATOR", "Test%sSetGenerator");
		addAbstractMapper("SIMPLE_TEST_GENERATOR", "Simple%sTestGenerator");
		addAbstractMapper("SIMPLE_QUEUE_TEST_GENERATOR", "Simple%sQueueTestGenerator");
		addAbstractBiMapper("SIMPLE_MAP_TEST_GENERATOR", "Simple%sMapTestGenerator", "2");
		addAbstractBiMapper("DERIVED_MAP_GENERATORS", "Derived%sMapGenerators", "2");
		addAbstractBiMapper("TEST_ORDERED_MAP_GENERATOR", "Test%sOrderedMapGenerator", "2");
		addAbstractBiMapper("TEST_SORTED_MAP_GENERATOR", "Test%sSortedMapGenerator", "2");
		addAbstractBiMapper("TEST_MAP_GENERATOR", "Test%sMapGenerator", "2");
		
		//Interfaces
		addClassMapper("LIST_ITERATOR", "ListIterator");
		addClassMapper("BI_ITERATOR", "BidirectionalIterator");
		addBiClassMapper("BI_CONSUMER", "Consumer", "");
		addClassMapper("BI_TO_OBJECT_CONSUMER", "ObjectConsumer");
		addAbstractMapper("BI_FROM_OBJECT_CONSUMER", "Object%sConsumer");
		addClassMapper("SPLIT_ITERATOR", "Splititerator");
		addClassMapper("ITERATOR", "Iterator");
		addClassMapper("ITERABLE", "Iterable");
		addClassMapper("COLLECTION", "Collection");
		addClassMapper("TO_OBJECT_FUNCTION", "2ObjectFunction");
		addBiClassMapper("FUNCTION", "Function", "2");
		addClassMapper("LIST_ITER", "ListIter");
		addClassMapper("LIST", "List");
		addBiClassMapper("NAVIGABLE_MAP", "NavigableMap", "2");
		addBiClassMapper("ORDERED_MAP", "OrderedMap", "2");
		addBiClassMapper("SORTED_MAP", "SortedMap", "2");
		addBiClassMapper("CONCURRENT_MAP", "ConcurrentMap", "2");
		addBiClassMapper("MAP", "Map", "2");
		addClassMapper("NAVIGABLE_SET", "NavigableSet");
		addBiClassMapper("PAIR", "Pair", "");
		addClassMapper("PRIORITY_QUEUE", "PriorityQueue");
		addClassMapper("PRIORITY_DEQUEUE", "PriorityDequeue");
		addClassMapper("PREDICATE", "2BooleanFunction");
		addClassMapper("SORTED_SET", "SortedSet");
		addClassMapper("ORDERED_SET", "OrderedSet");
		addClassMapper("SET", "Set");
		addClassMapper("STRATEGY", "Strategy");
		addClassMapper("STACK", "Stack");
		addClassMapper("SUPPLIER", "Supplier");
		addAbstractMapper("SINGLE_UNARY_OPERATOR", "%1$s%1$sUnaryOperator");
		addClassMapper("TASK", "Task");
		addBiClassMapper("UNARY_OPERATOR", "UnaryOperator", "");
		if(type.isObject())
		{
			if(!valueType.isObject()) addSimpleMapper("VALUE_CONSUMER", valueType.getFileType()+"Consumer");
			else addSimpleMapper("VALUE_CONSUMER", "Consumer");
			addSimpleMapper("CONSUMER", "Consumer");
			addSimpleMapper("IARRAY", "IObjectArray");
		}
		else
		{
			if(valueType.isObject()) 
			{
				addSimpleMapper("VALUE_CONSUMER", "Consumer");
				addSimpleMapper("CONSUMER", type.getFileType()+"Consumer");
			}
			else addClassMapper("CONSUMER", "Consumer");
			addFunctionMappers("IARRAY", "I%sArray");
		}
		addSimpleMapper("VALUE_COMPARATOR", valueType.isObject() ? "Comparator" : String.format("%sComparator", valueType.getNonFileType()));
		addSimpleMapper("COMPARATOR", type.isObject() ? "Comparator" : String.format("%sComparator", type.getNonFileType()));
		return this;
	}
	
	public GlobalVariables createFunctions()
	{
		addSimpleMapper("APPLY_KEY_VALUE", type.isObject() ? "apply" : "applyAs"+type.getNonFileType());
		addSimpleMapper("APPLY_VALUE", valueType.isObject() ? "apply" : "applyAs"+valueType.getNonFileType());
		addSimpleMapper("APPLY_CAST", "applyAs"+type.getCustomJDKType().getNonFileType());
		addSimpleMapper("APPLY", type.isObject() ? "apply" : "applyAs"+type.getNonFileType());
		addFunctionValueMapper("BULK_MERGE", "mergeAll");
		addFunctionValueMappers("COMPUTE_IF_ABSENT", "compute%sIfAbsent");
		addFunctionValueMappers("COMPUTE_IF_PRESENT", "compute%sIfPresent");
		addFunctionValueMapper("COMPUTE", "compute");
		addFunctionMapper("DEQUEUE_LAST", "dequeueLast");
		addFunctionMapper("DEQUEUE", "dequeue");
		addFunctionMappers("POLL_FIRST_ENTRY_KEY", "pollFirst%sKey");
		addFunctionMappers("POLL_LAST_ENTRY_KEY", "pollLast%sKey");
		addFunctionMapper("POLL_FIRST_KEY", "pollFirst");
		addFunctionMapper("POLL_LAST_KEY", "pollLast");
		addFunctionMappers("FIRST_ENTRY_KEY", "first%sKey");
		addFunctionValueMappers("FIRST_ENTRY_VALUE", "first%sValue");
		addFunctionMapper("FIRST_KEY", "first");
		addFunctionMappers("LAST_ENTRY_KEY", "last%sKey");
		addFunctionValueMappers("LAST_ENTRY_VALUE", "last%sValue");
		addFunctionMappers("ENTRY_KEY", "get%sKey");
		addFunctionValueMappers("ENTRY_VALUE", "get%sValue");
		addFunctionMappers("KEY_ENTRY", "set%sKey");
		addFunctionValueMappers("VALUE_ENTRY", "set%sValue");
		addFunctionMapper("GET_KEY", "get");
		if(type.isObject()) addFunctionValueMapper("GET_VALUE", valueType.isObject() ? "getObject" : "get");
		else addSimpleMapper("GET_VALUE", "get");
		addSimpleMapper("GET_JAVA", type.isObject() ? "get" : "getAs"+type.getCustomJDKType().getNonFileType());
		addFunctionMapper("LAST_KEY", "last");
		addFunctionValueMapper("MERGE", "merge");
		addFunctionMapper("NEXT", "next");
		addFunctionMapper("PREVIOUS", "previous");
		addFunctionMapper("REMOVE_SWAP", "swapRemove");
		if(type.isObject()) addFunctionMapper("REMOVE_VALUE", "rem");
		else addSimpleMapper("REMOVE_VALUE", "remove");
		addFunctionMapper("REMOVE_KEY", "rem");
		addFunctionMapper("REMOVE_LAST", "removeLast");
		addFunctionMapper("REMOVE", "remove");
		addFunctionValueMappers("REPLACE_VALUES", valueType.isObject() ? "replaceObjects" : "replace%ss");
		addFunctionMappers("REPLACE", type.isObject() ? "replaceObjects" : "replace%ss");
		addFunctionMappers("SORT", "sort%ss");
		addFunctionValueMappers("SUPPLY_IF_ABSENT", "supply%sIfAbsent");
		addSimpleMapper("VALUE_TEST_VALUE", valueType.isObject() ? "getBoolean" : "get");
		addSimpleMapper("TEST_VALUE", type.isObject() ? "getBoolean" : "get");
		addSimpleMapper("NEW_STREAM", type.isPrimitiveBlocking() ? "" : type.getCustomJDKType().getKeyType()+"Stream");
		addSimpleMapper("VALUE_TO_ARRAY", "to"+valueType.getNonFileType()+"Array");
		addSimpleMapper("TO_ARRAY", "to"+type.getNonFileType()+"Array");
		addSimpleMapper("[SPACE]", " ");
		operators.sort(Comparator.comparing(IMapper::getSearchValue, this::sort));
		return this;
	}
	
	public GlobalVariables createFlags()
	{
		flags.add("TYPE_"+type.getCapType());
		flags.add("VALUE_"+valueType.getCapType());
		if(type == valueType) flags.add("SAME_TYPE");
		if(type.hasFunction(valueType)) flags.add("JDK_FUNCTION");
		if(!type.needsCustomJDKType()) flags.add("JDK_TYPE");
		if(!type.isPrimitiveBlocking()) flags.add("PRIMITIVES");
		if(!valueType.isPrimitiveBlocking()) flags.add("VALUE_PRIMITIVES");
		if(valueType.needsCustomJDKType()) flags.add("JDK_VALUE");
		return this;
	}
	
	public TemplateProcess create(String fileName, String splitter, boolean valueOnly)
	{
		TemplateProcess process = new TemplateProcess(String.format(fileName+".java", (splitter != null ? type.getFileType()+splitter+valueType.getFileType() : (valueOnly ? valueType : type).getFileType())));
		process.setPathBuilder(new PathBuilder(type.getPathType()));
		process.addFlags(flags);
		process.addMappers(operators);
		return process;
	}
	
	public void testComparason(List<String> keys, List<String> values) {
		List<IMapper> copy = new ArrayList<>(operators);
		Collections.shuffle(copy);
		copy.sort(Comparator.comparing(IMapper::getSearchValue, this::sort));
		operators.stream().map(IMapper::getSearchValue).forEach(keys::add);
		copy.stream().map(IMapper::getSearchValue).forEach(values::add);
	}
	
	private int sort(String key, String value) {
		if(value.contains(key)) return 1;
		else if(key.contains(value)) return -1;
		return 0;
	}
	
	public ClassType getType()
	{
		return type;
	}
	
	private void addClassMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, valueType.getFileType()+replacement));
		operators.add(new SimpleMapper(pattern, pattern, type.getFileType()+replacement));
	}
	
	private void addBiClassMapper(String pattern, String replacement, String splitter)
	{
		operators.add(new SimpleMapper("KEY_"+pattern, "KEY_"+pattern, type.getFileType()+splitter+type.getFileType()+replacement));
		operators.add(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, valueType.getFileType()+splitter+valueType.getFileType()+replacement));
		operators.add(new SimpleMapper(pattern, pattern, type.getFileType()+splitter+valueType.getFileType()+replacement));
	}
	
	private void addAbstractMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, String.format(replacement, valueType.getFileType())));
		operators.add(new SimpleMapper(pattern, pattern, String.format(replacement, type.getFileType())));		
	}
	
	private void addAbstractBiMapper(String pattern, String replacement, String splitter) 
	{
		operators.add(new SimpleMapper(pattern, pattern, String.format(replacement, type.getFileType()+splitter+valueType.getFileType())));
	}
	
	private void addFunctionMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, replacement+valueType.getNonFileType()));
		operators.add(new SimpleMapper(pattern, pattern, replacement+type.getNonFileType()));
	}
	
	private void addFunctionValueMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(pattern, pattern, replacement+valueType.getNonFileType()));
	}
	
	private void addFunctionMappers(String pattern, String replacement)
	{
		operators.add(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, String.format(replacement, valueType.getNonFileType())));		
		operators.add(new SimpleMapper(pattern, pattern, String.format(replacement, type.getNonFileType())));		
	}
	
	private void addFunctionValueMappers(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(pattern, pattern, String.format(replacement, valueType.getNonFileType())));		
	}
	
	private void addSimpleMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(pattern, pattern, replacement));
	}
	
	private void addAnnontion(String pattern, String value)
	{
		if(type == ClassType.OBJECT) operators.add(new LineMapper(pattern, pattern));
		else operators.add(new SimpleMapper(pattern, pattern, value));
	}
	
	private void addValueAnnontion(String pattern, String value)
	{
		if(valueType == ClassType.OBJECT) operators.add(new LineMapper(pattern, pattern));
		else operators.add(new SimpleMapper(pattern, pattern, value));
	}
	
	private void addComment(String pattern, String value)
	{
		if(type == ClassType.OBJECT) operators.add(new InjectMapper(pattern, pattern, value).removeBraces());
		else operators.add(new LineMapper(pattern, pattern));
	}
	
	private void addValueComment(String pattern, String value)
	{
		if(valueType == ClassType.OBJECT) operators.add(new InjectMapper(pattern, pattern, value).removeBraces());
		else operators.add(new LineMapper(pattern, pattern));
	}
	
	private InjectMapper addInjectMapper(String pattern, String replacement)
	{
		InjectMapper mapper = new InjectMapper(pattern, pattern, replacement);
		operators.add(mapper);
		return mapper;
	}
	
	private ArgumentMapper addArgumentMapper(String pattern, String replacement)
	{
		return addArgumentMapper(pattern, replacement, ", ");
	}
	
	private ArgumentMapper addArgumentMapper(String pattern, String replacement, String splitter)
	{
		ArgumentMapper mapper = new ArgumentMapper(pattern, pattern, replacement, splitter);
		operators.add(mapper);
		return mapper;
	}
	
	class PathBuilder implements UnaryOperator<Path>
	{
		String before;
		
		public PathBuilder(String before)
		{
			this.before = before;
		}
		
		@Override
		public Path apply(Path t)
		{
			return t.subpath(0, 6).resolve(before).resolve(t.subpath(6, t.getNameCount()));
		}
	}
}
