package speiger.src.builder.processor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public class TemplateProcess
{
	Path path;
	String fileName;
	Set<String> parsePool = new HashSet<>();
	List<UnaryOperator<String>> mappers = new ArrayList<>();
	
	public TemplateProcess(String fileName, Path path)
	{
		this.fileName = fileName;
		this.path = path;
	}
	
	public void addFlags(String...flags)
	{
		parsePool.addAll(Arrays.asList(flags));
	}
	
	public void addMapper(UnaryOperator<String> mapper)
	{
		mappers.add(mapper);
	}
	
	public void addMappers(Collection<UnaryOperator<String>> mappers)
	{
		this.mappers.addAll(mappers);
	}
}
