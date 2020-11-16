package speiger.src.builder.example;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import speiger.src.builder.mappers.ArgumentMapper;
import speiger.src.builder.mappers.InjectMapper;
import speiger.src.builder.mappers.SimpleMapper;
import speiger.src.builder.processor.TemplateProcess;

public class GlobalVariables
{
	List<UnaryOperator<String>> operators = new ArrayList<>();
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
		addSimpleMapper(" KEY_GENERIC_TYPE", type == ClassType.OBJECT ? "<"+type.getKeyType()+">" : "");
		addSimpleMapper(" GENERIC_BRACES", type == ClassType.OBJECT ? " <"+type.getKeyType()+">" : "");
		addSimpleMapper("BRACES", type == ClassType.OBJECT ? "<>" : "");
		if(type.needsCustomJDKType())
		{
			addSimpleMapper("JAVA_TYPE", type.getCustomJDKType().getKeyType());
			addSimpleMapper("SANITY_CAST", "castTo"+type.getFileType());
		}
		return this;
	}
	
	public GlobalVariables createHelperVariables()
	{
		addArgumentMapper("EQUALS_KEY_TYPE", type == ClassType.OBJECT ? "Objects.equals(%1$s, %2$s)" : "Objects.equals(KEY_TO_OBJ(%1$s), %2$s)").removeBraces();
		addArgumentMapper("EQUALS", type.getEquals()).removeBraces();
		addSimpleMapper("KEY_TO_OBJ", type.getClassType()+".valueOf");
		addInjectMapper("OBJ_TO_KEY", type == ClassType.OBJECT ? "%s" : "%s."+type.getKeyType()+"Value()").removeBraces();
		addInjectMapper("CLASS_TO_KEY", "(("+type.getClassType()+")%s)."+type.getKeyType()+"Value()").removeBraces();
		return this;
	}
	
	public GlobalVariables createClassTypes()
	{
		addSimpleMapper("JAVA_PREDICATE", type.isPrimitiveBlocking() ? "" : type.getCustomJDKType().getFileType()+"Predicate");
		addSimpleMapper("JAVA_CONSUMER", type.isPrimitiveBlocking() ? "" : "java.util.function."+type.getCustomJDKType().getFileType()+"Consumer");
		addClassMapper("CONSUMER", "Consumer");
		addClassMapper("ITERATORS", "Iterators");
		addClassMapper("BI_ITERATOR", "BidirectionalIterator");
		addClassMapper("LIST_ITERATOR", "ListIterator");
		addClassMapper("ITERATOR", "Iterator");
		addClassMapper("ITERABLE", "Iterable");
		addClassMapper("ABSTRACT_COLLECTION", "AbstractCollection");
		addClassMapper("COLLECTION", "Collection");
		addClassMapper("ARRAYS", "Arrays");
		addClassMapper("ABSTRACT_LIST", "AbstractList");
		addClassMapper("LIST_ITER", "ListIter");
		addClassMapper("SUB_LIST", "SubList");
		addClassMapper("ARRAY_LIST", "ArrayList");
		addClassMapper("LIST", "List");
		return this;
	}
	
	public GlobalVariables createFunctions()
	{
		addFunctionMapper("NEXT", "next");
		addSimpleMapper("TO_ARRAY", "to"+type.getNonFileType()+"Array");
		addFunctionMapper("GET_KEY", "get");
		addFunctionMapper("REMOVE_KEY", "rem");
		addFunctionMapper("REMOVE", "remove");
		addFunctionMapper("PREVIOUS", "previous");
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
		TemplateProcess process = new TemplateProcess(type.getFileType()+fileName+".java");
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
		operators.add(new SimpleMapper(pattern, type.getFileType()+replacement));		
	}
	
	private void addFunctionMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(pattern, replacement+type.getNonFileType()));
	}
	
	private void addSimpleMapper(String pattern, String replacement)
	{
		operators.add(new SimpleMapper(pattern, replacement));
	}
	
	private InjectMapper addInjectMapper(String pattern, String replacement)
	{
		InjectMapper mapper = new InjectMapper(pattern, replacement);
		operators.add(mapper);
		return mapper;
	}
	
	private ArgumentMapper addArgumentMapper(String pattern, String replacement)
	{
		return addArgumentMapper(pattern, replacement, ", ");
	}
	
	private ArgumentMapper addArgumentMapper(String pattern, String replacement, String splitter)
	{
		ArgumentMapper mapper = new ArgumentMapper(pattern, replacement, splitter);
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
