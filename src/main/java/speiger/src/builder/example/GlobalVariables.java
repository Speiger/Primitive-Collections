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
		operators.add(new SimpleMapper("PACKAGE", type.getPathType()));
		operators.add(new SimpleMapper("CLASS_TYPE", type.getClassType()));
		operators.add(new SimpleMapper("KEY_TYPE", type.getKeyType()));
		operators.add(new SimpleMapper(" KEY_GENERIC_TYPE", type == ClassType.OBJECT ? "<"+type.getKeyType()+">" : ""));
		if(type.needsCustomJDKType())
		{
			operators.add(new SimpleMapper("JAVA_TYPE", type.getCustomJDKType().getKeyType()));
			operators.add(new SimpleMapper("SANITY_CAST", "castTo"+type.getFileType()));
		}
		operators.add(new SimpleMapper("KEY_TO_OBJ", type.getClassType()+".valueOf"));
		operators.add(new InjectMapper("OBJ_TO_KEY(\\([^)]+\\)|\\S)", "%s."+type.getKeyType()+"Value()").removeBraces());
		return this;
	}
	
	public GlobalVariables createClassTypes()
	{
		operators.add(new SimpleMapper("JAVA_CONSUMER", type.isPrimitiveBlocking() ? "" : "java.util.function."+type.getCustomJDKType().getFileType()+"Consumer"));
		operators.add(new SimpleMapper("CONSUMER", type.getFileType()+"Consumer"));
		operators.add(new SimpleMapper("ITERATOR", type.getFileType()+"Iterator"));
		operators.add(new SimpleMapper("ITERABLE", type.getFileType()+"Iterable"));
		return this;
	}
	
	public GlobalVariables createFunctions()
	{
		operators.add(new SimpleMapper("NEXT()", "next"+type.getNonClassType()+"()"));
		return this;
	}
	
	public GlobalVariables createFlags()
	{
		flags.add("TYPE_"+type.getCapType());
		if(!type.needsCustomJDKType())
		{
			flags.add("JDK_CONSUMER");
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
