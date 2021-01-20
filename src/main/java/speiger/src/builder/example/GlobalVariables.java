package speiger.src.builder.example;

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
	
	public GlobalVariables(ClassType type)
	{
		this.type = type;
	}
	
	public GlobalVariables createVariables()
	{
		addSimpleMapper("PACKAGE", type.getPathType());
		addSimpleMapper("CLASS_TYPE", type.getClassType());
		addSimpleMapper("KEY_TYPE", type.getKeyType());
		addSimpleMapper("EMPTY_VALUE", type.getEmptyValue());
		addSimpleMapper(" KEY_GENERIC_TYPE", type.isObject() ? "<"+type.getKeyType()+">" : "");
		addSimpleMapper(" NO_GENERIC_TYPE", type.isObject() ? "<?>" : "");
		addSimpleMapper(" KEY_COMPAREABLE_TYPE", type.isObject() ? "<"+type.getKeyType()+" extends Comparable<T>>" : "");
		addSimpleMapper(" KEY_SUPER_GENERIC_TYPE", type.isObject() ? "<? super "+type.getKeyType()+">" : "");
		addSimpleMapper(" GENERIC_BRACES", type.isObject() ? " <"+type.getKeyType()+">" : "");
		addSimpleMapper(" COMPAREABLE_BRACES", type.isObject() ? " <"+type.getKeyType()+" extends Comparable<T>>" : "");
		addSimpleMapper("BRACES", type.isObject() ? "<>" : "");
		if(type.needsCustomJDKType())
		{
			addSimpleMapper("JAVA_TYPE", type.getCustomJDKType().getKeyType());
			addSimpleMapper("SANITY_CAST", "castTo"+type.getFileType());
		}
		addAnnontion("@PrimitiveOverride", "@Override");
		addSimpleMapper("@PrimitiveDoc", "");
		addAnnontion("@Primitive", "@Deprecated");
		return this;
	}
	
	public GlobalVariables createHelperVariables()
	{
		addArgumentMapper("EQUALS_KEY_TYPE", type.isObject() ? "Objects.equals(%2$s, %1$s)" : "Objects.equals(%2$s, KEY_TO_OBJ(%1$s))").removeBraces();
		addInjectMapper("EQUALS_NOT_NULL", type.getComparableValue()+" != "+(type.isPrimitiveBlocking() ? type.getEmptyValue() : (type.needsCast() ? type.getEmptyValue() : "0"))).removeBraces();
		addInjectMapper("EQUALS_NULL", type.getComparableValue()+" == "+(type.isPrimitiveBlocking() ? type.getEmptyValue() : (type.needsCast() ? type.getEmptyValue() : "0"))).removeBraces();
		addArgumentMapper("EQUALS_NOT", type.getEquals(true)).removeBraces();
		addArgumentMapper("EQUALS", type.getEquals(false)).removeBraces();
		addArgumentMapper("COMPARE_TO_KEY", type.isObject() ? "((Comparable<T>)%1$s).compareTo((T)%2$s)" : type.getClassType()+".compare(%1$s, %2$s)").removeBraces();
		addArgumentMapper("COMPARE_TO", type.isObject() ? "%1$s.compareTo(%2$s)" : type.getClassType()+".compare(%1$s, %2$s)").removeBraces();
		addInjectMapper("KEY_TO_OBJ", type.isObject() ? "%s" : type.getClassType()+".valueOf(%s)").removeBraces();
		addInjectMapper("OBJ_TO_KEY", type.isObject() ? "%s" : "%s."+type.getKeyType()+"Value()").removeBraces();
		addInjectMapper("CLASS_TO_KEY", "(("+type.getClassType()+")%s)."+type.getKeyType()+"Value()").removeBraces();
		addSimpleMapper("APPLY", "applyAs"+type.getCustomJDKType().getNonFileType());
		addInjectMapper("TO_HASH", type.isObject() ? "%s.hashCode()" : type.getClassType()+".hashCode(%s)").removeBraces();
		addSimpleMapper("CAST_KEY_ARRAY ", type.isObject() ? "(KEY_TYPE[])" : "");
		addSimpleMapper("EMPTY_KEY_ARRAY", type.isObject() ? "(KEY_TYPE[])ARRAYS.EMPTY_ARRAY" : "ARRAYS.EMPTY_ARRAY");
		addInjectMapper("NEW_KEY_ARRAY", type.isObject() ? "(KEY_TYPE[])new Object[%s]" : "new KEY_TYPE[%s]").removeBraces();
		addInjectMapper("NEW_CLASS_ARRAY", type.isObject() ? "(CLASS_TYPE[])new Object[%s]" : "new CLASS_TYPE[%s]").removeBraces();
		return this;
	}
	
	public GlobalVariables createClassTypes()
	{
		addSimpleMapper("JAVA_PREDICATE", type.isPrimitiveBlocking() ? "" : type.getCustomJDKType().getFileType()+"Predicate");
		addSimpleMapper("JAVA_CONSUMER", type.isPrimitiveBlocking() ? "" : "java.util.function."+type.getCustomJDKType().getFileType()+"Consumer");
		addSimpleMapper("UNARY_OPERATOR", type.isObject() ? "" : type == ClassType.BOOLEAN ? "BinaryOperator" : type.getCustomJDKType().getFileType()+"UnaryOperator");
		
		//Final Classes
		addClassMapper("ARRAY_LIST", "ArrayList");
		addClassMapper("ARRAY_FIFO_QUEUE", "ArrayFIFOQueue");
		addClassMapper("ARRAY_PRIORITY_QUEUE", "ArrayPriorityQueue");
		addClassMapper("HEAP_PRIORITY_QUEUE", "HeapPriorityQueue");
		addClassMapper("LINKED_CUSTOM_HASH_SET", "LinkedOpenCustomHashSet");
		addClassMapper("LINKED_HASH_SET", "LinkedOpenHashSet");
		addClassMapper("CUSTOM_HASH_SET", "OpenCustomHashSet");
		addClassMapper("HASH_SET", "OpenHashSet");
		addClassMapper("RB_TREE_SET", "RBTreeSet");
		addClassMapper("AVL_TREE_SET", "AVLTreeSet");
		addClassMapper("ARRAY_SET", "ArraySet");
		
		//Abstract Classes
		addAbstractMapper("ABSTRACT_COLLECTION", "Abstract%sCollection");
		addAbstractMapper("ABSTRACT_SET", "Abstract%sSet");
		addAbstractMapper("ABSTRACT_LIST", "Abstract%sList");
		addClassMapper("SUB_LIST", "SubList");
		
		//Helper Classes
		addClassMapper("LISTS", "Lists");
		addClassMapper("SETS", "Sets");
		addClassMapper("COLLECTIONS", "Collections");
		addClassMapper("ARRAYS", "Arrays");
		addClassMapper("ITERATORS", "Iterators");
		
		//Interfaces
		addClassMapper("LIST_ITERATOR", "ListIterator");
		addClassMapper("BI_ITERATOR", "BidirectionalIterator");
		addClassMapper("ITERATOR", "Iterator");
		addClassMapper("ITERABLE", "Iterable");
		addClassMapper("COLLECTION", "Collection");
		addClassMapper("LIST_ITER", "ListIter");
		addClassMapper("LIST", "List");
		addClassMapper("NAVIGABLE_SET", "NavigableSet");
		addClassMapper("PRIORITY_QUEUE", "PriorityQueue");
		addClassMapper("PRIORITY_DEQUEUE", "PriorityDequeue");
		addClassMapper("SORTED_SET", "SortedSet");
		addClassMapper("SET", "Set");
		addClassMapper("STRATEGY", "Strategy");
		addClassMapper("STACK", "Stack");
		if(type.isObject())
		{
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
		addFunctionMapper("NEXT", "next");
		addSimpleMapper("TO_ARRAY", "to"+type.getNonFileType()+"Array");
		addFunctionMapper("GET_KEY", "get");
		addFunctionMapper("ENQUEUE_FIRST", "enqueueFirst");
		addFunctionMapper("ENQUEUE", "enqueue");
		addFunctionMapper("DEQUEUE_LAST", "dequeueLast");
		addFunctionMapper("DEQUEUE", "dequeue");
		addFunctionMapper("REMOVE_KEY", "rem");
		addFunctionMapper("REMOVE_LAST", "removeLast");
		addFunctionMapper("REMOVE", "remove");
		addFunctionMapper("PREVIOUS", "previous");
		addFunctionMapper("PEEK", "peek");
		addFunctionMapper("POP", "pop");
		addFunctionMapper("PUSH", "push");
		addFunctionMapper("TOP", "top");
		addFunctionMappers("REPLACE", "replace%ss");
		addFunctionMappers("SORT", "sort%ss");
		addFunctionMapper("POLL_FIRST_KEY", "pollFirst");
		addFunctionMapper("FIRST_KEY", "first");
		addFunctionMapper("POLL_LAST_KEY", "pollLast");
		addFunctionMapper("LAST_KEY", "last");
		return this;
	}
	
	public GlobalVariables createFlags()
	{
		flags.add("TYPE_"+type.getCapType());
		if(!type.needsCustomJDKType())
		{
			flags.add("JDK_CONSUMER");
		}
		if(!type.isPrimitiveBlocking())
		{
			flags.add("PRIMITIVES");
		}
		return this;
	}
	
	public TemplateProcess create(String fileName)
	{
		TemplateProcess process = new TemplateProcess(String.format(fileName+".java", type.getFileType()));
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
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, type.getFileType()+replacement));		
	}
	
	private void addAbstractMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, String.format(replacement, type.getFileType())));		
	}
	
	private void addFunctionMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, replacement+type.getNonFileType()));
	}
	
	private void addFunctionMappers(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(type.name()+"["+pattern+"]", pattern, String.format(replacement, type.getNonFileType())));		
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
