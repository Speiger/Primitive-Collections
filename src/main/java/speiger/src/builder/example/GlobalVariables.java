package speiger.src.builder.example;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

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
		if(type.needsCustomJDKType())
		{
			addSimpleMapper("JAVA_TYPE", type.getCustomJDKType().getKeyType());
			addSimpleMapper("SANITY_CAST", "castTo"+type.getFileType());
		}
		addSimpleMapper("KEY_TO_OBJ", type.getClassType()+".valueOf");
		addInjectMapper("OBJ_TO_KEY(\\([^)]+\\)|\\S)", "%s."+type.getKeyType()+"Value()").removeBraces();
		addInjectMapper("CLASS_TO_KEY(\\([^)]+\\)|\\S)", "(("+type.getClassType()+")%s)."+type.getKeyType()+"Value()").removeBraces();
		
		return this;
	}
	
	public GlobalVariables createClassTypes()
	{
		addSimpleMapper("JAVA_PREDICATE", type.isPrimitiveBlocking() ? "" : type.getCustomJDKType().getFileType()+"Predicate");
		addSimpleMapper("JAVA_CONSUMER", type.isPrimitiveBlocking() ? "" : "java.util.function."+type.getCustomJDKType().getFileType()+"Consumer");
		addSimpleMapper("CONSUMER", type.getFileType()+"Consumer");
		addSimpleMapper("ITERATOR", type.getFileType()+"Iterator");
		addSimpleMapper("ITERABLE", type.getFileType()+"Iterable");
		addSimpleMapper("ABSTRACT_COLLECTION", type.getFileType()+"AbstractCollection");
		addSimpleMapper("COLLECTION", type.getFileType()+"Collection");
		return this;
	}
	
	public GlobalVariables createFunctions()
	{
		addSimpleMapper("NEXT()", "next"+type.getNonClassType()+"()");
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
