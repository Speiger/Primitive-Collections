package speiger.src.builder;

import java.nio.file.Path;
import java.util.ArrayList;
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
		addSimpleMapper("VALUE_TYPE", valueType.getValueType());
		
		addSimpleMapper("EMPTY_KEY_VALUE", type.getEmptyValue());
		addSimpleMapper("EMPTY_VALUE", valueType.getEmptyValue());

		addSimpleMapper(" KEY_GENERIC_TYPE", type.isObject() ? "<"+type.getKeyType()+">" : "");
		addSimpleMapper(" KEY_KEY_GENERIC_TYPE", type.isObject() ? "<"+type.getKeyType()+", "+type.getKeyType()+">" : "");
		addSimpleMapper(" VALUE_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+">" : "");
		addSimpleMapper(" VALUE_VALUE_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+", "+valueType.getValueType()+">" : "");
		addSimpleMapper(" KEY_VALUE_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+", "+valueType.getValueType()+">" : "<"+type.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" KEY_VALUE_VALUE_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+", "+valueType.getValueType()+", "+valueType.getValueType()+">" : "<"+type.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+", "+valueType.getValueType()+">" : ""));
		addSimpleMapper(" NO_GENERIC_TYPE", type.isObject() ? "<?>" : "");
		addSimpleMapper(" KEY_COMPAREABLE_TYPE", type.isObject() ? "<"+type.getKeyType()+" extends Comparable<T>>" : "");
		addSimpleMapper(" KEY_SUPER_GENERIC_TYPE", type.isObject() ? "<? super "+type.getKeyType()+">" : "");
		addSimpleMapper(" VALUE_SUPER_GENERIC_TYPE", valueType.isObject() ? "<? super "+valueType.getValueType()+">" : "");
		addSimpleMapper(" KEY_VALUE_SUPER_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<? super "+type.getKeyType()+", ? super "+valueType.getValueType()+">" : "<? super "+type.getKeyType()+">") : (valueType.isObject() ? "<? super "+valueType.getValueType()+">" : ""));
		addSimpleMapper(" KEY_ENUM_VALUE_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+" extends Enum<"+type.getKeyType()+">, "+valueType.getValueType()+">" : "<"+type.getKeyType()+" extends Enum<"+type.getKeyType()+">>") : (valueType.isObject() ? "<"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" KEY_VALUE_ENUM_GENERIC_TYPE", type.isObject() ? (valueType.isObject() ? "<"+type.getKeyType()+", "+valueType.getValueType()+" extends Enum<"+valueType.getValueType()+">>" : "<"+type.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+" extends Enum<"+valueType.getValueType()+">>" : ""));

		addSimpleMapper(" GENERIC_KEY_BRACES", type.isObject() ? " <"+type.getKeyType()+">" : "");
		addSimpleMapper(" GENERIC_VALUE_BRACES", type.isObject() ? " <"+valueType.getValueType()+">" : "");
		addSimpleMapper(" GENERIC_KEY_VALUE_BRACES", type.isObject() ? (valueType.isObject() ? " <"+type.getKeyType()+", "+valueType.getValueType()+">" : " <"+type.getKeyType()+">") : (valueType.isObject() ? " <"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" COMPAREABLE_KEY_BRACES", type.isObject() ? " <"+type.getKeyType()+" extends Comparable<T>>" : "");
		addSimpleMapper("KV_BRACES", type.isObject() || valueType.isObject() ? "<>" : "");
		addSimpleMapper("BRACES", type.isObject() ? "<>" : "");
		if(type.needsCustomJDKType())
		{
			addSimpleMapper("JAVA_TYPE", type.getCustomJDKType().getKeyType());
			addSimpleMapper("SANITY_CAST", "castTo"+type.getFileType());
		}
		if(valueType.needsCustomJDKType())
		{
			addSimpleMapper("SANITY_CAST_VALUE", "castTo"+valueType.getFileType());
		}
		addAnnontion("@PrimitiveOverride", "@Override");
		addSimpleMapper("@PrimitiveDoc", "");
		addAnnontion("@Primitive", "@Deprecated");
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
		
		addArgumentMapper("COMPAREABLE_TO_"+fix, type.isObject() ? "((Comparable<"+type.getKeyType(value)+">)%1$s).compareTo(("+type.getKeyType(value)+")%2$s)" : type.getClassType(value)+".compare(%1$s, %2$s)").removeBraces();
		addArgumentMapper("COMPARE_TO_"+fix, type.isObject() ? "%1$s.compareTo(%2$s)" : type.getClassType(value)+".compare(%1$s, %2$s)").removeBraces();
		
		addInjectMapper(fix+"_TO_OBJ", type.isObject() ? "%s" : type.getClassType(value)+".valueOf(%s)").removeBraces();
		addInjectMapper("OBJ_TO_"+fix, type.isObject() ? "%s" : "%s."+type.getKeyType(value)+"Value()").removeBraces();
		addInjectMapper("CLASS_TO_"+fix, type.isObject() ? "("+type.getKeyType(value)+")%s" : "(("+type.getClassType(value)+")%s)."+type.getKeyType(value)+"Value()").removeBraces();
		
		addInjectMapper(fix+"_TO_HASH", type.isObject() ? "%s.hashCode()" : type.getClassType(value)+".hashCode(%s)").removeBraces();
		addInjectMapper(fix+"_TO_STRING", type.isObject() ? "%s.toString()" : type.getClassType(value)+".toString(%s)").removeBraces();
		
		addSimpleMapper("CAST_"+fix+"_ARRAY ", type.isObject() ? "("+fix+"_TYPE[])" : "");
		addSimpleMapper("EMPTY_"+fix+"_ARRAY", type.isObject() ? "("+fix+"_TYPE[])ARRAYS.EMPTY_ARRAY" : "ARRAYS.EMPTY_ARRAY");
		addInjectMapper("NEW_"+fix+"_ARRAY", type.isObject() ? "("+fix+"_TYPE[])new Object[%s]" : "new "+fix+"_TYPE[%s]").removeBraces();
		addInjectMapper("NEW_CLASS"+(value ? "_VALUE" : "")+"_ARRAY", type.isObject() ? "(CLASS_TYPE[])new Object[%s]" : "new CLASS_TYPE[%s]").removeBraces();
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
		addSimpleMapper("JAVA_FUNCTION", type.getFunctionClass(valueType));
		addSimpleMapper("JAVA_BINARY_OPERATOR", type == ClassType.BOOLEAN ? "" : (type.isObject() ? "java.util.function.BinaryOperator" : "java.util.function."+type.getCustomJDKType().getFileType()+"BinaryOperator"));
		addSimpleMapper("JAVA_UNARY_OPERATOR", type.isObject() ? "BinaryOperator" : type == ClassType.BOOLEAN ? "" : type.getCustomJDKType().getFileType()+"UnaryOperator");
		
		//Final Classes
		addClassMapper("ARRAY_LIST", "ArrayList");
		addClassMapper("ARRAY_FIFO_QUEUE", "ArrayFIFOQueue");
		addClassMapper("ARRAY_PRIORITY_QUEUE", "ArrayPriorityQueue");
		addClassMapper("HEAP_PRIORITY_QUEUE", "HeapPriorityQueue");
		addClassMapper("LINKED_CUSTOM_HASH_SET", "LinkedOpenCustomHashSet");
		addClassMapper("LINKED_HASH_SET", "LinkedOpenHashSet");
		addClassMapper("CUSTOM_HASH_SET", "OpenCustomHashSet");
		addClassMapper("HASH_SET", "OpenHashSet");
		addBiClassMapper("LINKED_CUSTOM_HASH_MAP", "LinkedOpenCustomHashMap", "2");
		addBiClassMapper("LINKED_HASH_MAP", "LinkedOpenHashMap", "2");
		addBiClassMapper("CUSTOM_HASH_MAP", "OpenCustomHashMap", "2");
		addBiClassMapper("AVL_TREE_MAP", "AVLTreeMap", "2");
		addBiClassMapper("RB_TREE_MAP", "RBTreeMap", "2");
		addFunctionValueMappers("ENUM_MAP", valueType.isObject() ? "Enum2ObjectMap" : "Enum2%sMap");
		addBiClassMapper("HASH_MAP", "OpenHashMap", "2");
		addBiClassMapper("ARRAY_MAP", "ArrayMap", "2");
		addClassMapper("RB_TREE_SET", "RBTreeSet");
		addClassMapper("AVL_TREE_SET", "AVLTreeSet");
		addClassMapper("ARRAY_SET", "ArraySet");
		
		//Abstract Classes
		addAbstractMapper("ABSTRACT_COLLECTION", "Abstract%sCollection");
		addAbstractMapper("ABSTRACT_SET", "Abstract%sSet");
		addAbstractMapper("ABSTRACT_LIST", "Abstract%sList");
		addAbstractBiMapper("ABSTRACT_MAP", "Abstract%sMap", "2");
		addClassMapper("SUB_LIST", "SubList");
		
		//Helper Classes
		addClassMapper("LISTS", "Lists");
		addClassMapper("SETS", "Sets");
		addClassMapper("COLLECTIONS", "Collections");
		addClassMapper("ARRAYS", "Arrays");
		addClassMapper("ITERATORS", "Iterators");
		addBiClassMapper("MAPS", "Maps", "2");
		
		//Interfaces
		addClassMapper("LIST_ITERATOR", "ListIterator");
		addClassMapper("BI_ITERATOR", "BidirectionalIterator");
		addBiClassMapper("BI_CONSUMER", "Consumer", "");
		addClassMapper("ITERATOR", "Iterator");
		addClassMapper("ITERABLE", "Iterable");
		addClassMapper("COLLECTION", "Collection");
		addBiClassMapper("FUNCTION", "Function", "2");
		addClassMapper("LIST_ITER", "ListIter");
		addClassMapper("LIST", "List");
		addBiClassMapper("NAVIGABLE_MAP", "NavigableMap", "2");
		addBiClassMapper("SORTED_MAP", "SortedMap", "2");
		addBiClassMapper("MAP", "Map", "2");
		addClassMapper("NAVIGABLE_SET", "NavigableSet");
		addClassMapper("PRIORITY_QUEUE", "PriorityQueue");
		addClassMapper("PRIORITY_DEQUEUE", "PriorityDequeue");
		addClassMapper("SORTED_SET", "SortedSet");
		addClassMapper("SET", "Set");
		addClassMapper("STRATEGY", "Strategy");
		addClassMapper("STACK", "Stack");
		addBiClassMapper("UNARY_OPERATOR", "UnaryOperator", "");
		if(type.isObject())
		{
			if(!valueType.isObject())
			{
				addSimpleMapper("VALUE_CONSUMER", valueType.getFileType()+"Consumer");
			}
			addSimpleMapper("CONSUMER", "Consumer");
			addSimpleMapper("COMPARATOR", "Comparator");	
			addSimpleMapper("IARRAY", "IObjectArray");
		}
		else
		{
			addClassMapper("CONSUMER", "Consumer");
			addClassMapper("COMPARATOR", "Comparator");
			addFunctionMappers("IARRAY", "I%sArray");
		}
		return this;
	}
	
	public GlobalVariables createFunctions()
	{
		addSimpleMapper("APPLY_VALUE", valueType.isObject() ? "apply" : "applyAs"+valueType.getNonFileType());
		addSimpleMapper("APPLY_CAST", "applyAs"+type.getCustomJDKType().getNonFileType());
		addSimpleMapper("APPLY", type.isObject() ? "apply" : "applyAs"+type.getNonFileType());
		addFunctionValueMappers("COMPUTE_IF_ABSENT", "compute%sIfAbsent");
		addFunctionValueMappers("COMPUTE_IF_PRESENT", "compute%sIfPresent");
		addFunctionValueMapper("COMPUTE", "compute");
		addFunctionMapper("ENQUEUE_FIRST", "enqueueFirst");
		addFunctionMapper("ENQUEUE", "enqueue");
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
		addFunctionMapper("GET_KEY", "get");
		addFunctionValueMapper("GET_VALUE", valueType.isObject() ? "getObject" : "get");
		addFunctionMapper("LAST_KEY", "last");
		addFunctionValueMapper("MERGE", "merge");
		addFunctionMapper("NEXT", "next");
		addFunctionMapper("PREVIOUS", "previous");
		addFunctionMapper("PEEK", "peek");
		addFunctionMapper("POP", "pop");
		addFunctionMapper("PUSH", "push");
		addFunctionMapper("REMOVE_KEY", "rem");
		addFunctionMapper("REMOVE_LAST", "removeLast");
		addFunctionMapper("REMOVE", "remove");
		addFunctionValueMappers("REPLACE_VALUES", valueType.isObject() ? "replaceObjects" : "replace%ss");
		addFunctionMappers("REPLACE", type.isObject() ? "replaceObjects" : "replace%ss");
		addFunctionMappers("SORT", "sort%ss");
		addSimpleMapper("TO_ARRAY", "to"+type.getNonFileType()+"Array");
		addFunctionMapper("TOP", "top");
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
	
	public ClassType getType()
	{
		return type;
	}
	
	private void addClassMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"[VALUE_"+pattern+"]", "VALUE_"+pattern, valueType.getFileType()+replacement));
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, type.getFileType()+replacement));
	}
	
	private void addBiClassMapper(String pattern, String replacement, String splitter)
	{
		operators.add(new SimpleMapper(type.name()+"[KEY_"+pattern+"]", "KEY_"+pattern, type.getFileType()+splitter+type.getFileType()+replacement));
		operators.add(new SimpleMapper(type.name()+"[VALUE_"+pattern+"]", "VALUE_"+pattern, valueType.getFileType()+splitter+valueType.getFileType()+replacement));
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, type.getFileType()+splitter+valueType.getFileType()+replacement));
	}
	
	private void addAbstractMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"[VALUE_"+pattern+"]", "VALUE_"+pattern, String.format(replacement, valueType.getFileType())));
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, String.format(replacement, type.getFileType())));		
	}
	
	private void addAbstractBiMapper(String pattern, String replacement, String splitter) 
	{
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, String.format(replacement, type.getFileType()+splitter+valueType.getFileType())));
	}
	
	private void addFunctionMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"[VALUE_"+pattern+"]", "VALUE_"+pattern, replacement+valueType.getNonFileType()));
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, replacement+type.getNonFileType()));
	}
	
	private void addFunctionValueMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, replacement+valueType.getNonFileType()));
	}
	
	private void addFunctionMappers(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"[VALUE_"+pattern+"]", "VALUE_"+pattern, String.format(replacement, valueType.getNonFileType())));		
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, String.format(replacement, type.getNonFileType())));		
	}
	
	private void addFunctionValueMappers(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, String.format(replacement, valueType.getNonFileType())));		
	}
	
	private void addSimpleMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, replacement));
	}
	
	private void addAnnontion(String pattern, String value)
	{
		if(type == ClassType.OBJECT) operators.add(new LineMapper(type.name()+"["+pattern+"]", pattern));
		else operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, value));
	}
	
	private InjectMapper addInjectMapper(String pattern, String replacement)
	{
		InjectMapper mapper = new InjectMapper(type.name()+"["+pattern+"]", pattern, replacement);
		operators.add(mapper);
		return mapper;
	}
	
	private ArgumentMapper addArgumentMapper(String pattern, String replacement)
	{
		return addArgumentMapper(pattern, replacement, ", ");
	}
	
	private ArgumentMapper addArgumentMapper(String pattern, String replacement, String splitter)
	{
		ArgumentMapper mapper = new ArgumentMapper(type.name()+"["+pattern+"]", pattern, replacement, splitter);
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
