package speiger.src.builder.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.UnaryOperator;

import speiger.src.builder.misc.FileUtils;

public class Template
{
	String fileName;
	String textFile;
	List<ConditionedSegment> segments;
	
	public Template(String fileName, String textFile, List<ConditionedSegment> segments)
	{
		this.fileName = fileName;
		this.textFile = textFile;
		this.segments = segments;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public String build(Set<String> parsePool, List<UnaryOperator<String>> mappers)
	{
		StringBuilder builder = new StringBuilder(textFile);
		for(int i = 0,offset=0,m=segments.size();i<m;i++)
		{
			offset += segments.get(i).build(parsePool, builder, offset);
		}
		String result = builder.toString();
		for(int i = 0,m=mappers.size();i<m;i++)
		{
			result = mappers.get(i).apply(result);
		}
//		String s = "CLASS_TO_OBJ(\\([^)]+\\)|\\S)";
		return result;
	}
	
	public static Template parse(Path file) throws IOException
	{
		List<ConditionedSegment> segments = new ArrayList<ConditionedSegment>();
		StringJoiner joiner = new StringJoiner("\n");
		List<String> lines = Files.readAllLines(file);
		for(int i = 0;i<lines.size();i++)
		{
			String s = lines.get(i);
			String trimmed = s.trim();
			if(trimmed.startsWith("#"))
			{
				if(trimmed.startsWith("#if"))
				{
					i += ConditionedSegment.parse(s.trim().substring(3).trim(), lines, i, joiner.length(), segments);
					continue;					
				}
				else if(trimmed.startsWith("#symlink"))
				{
					return Template.parse(file.getParent().resolve(trimmed.substring(8).trim()));
				}
			}
			joiner.add(s);
		}
		return new Template(FileUtils.getFileName(file.getFileName()), joiner.toString(), segments);
	}
}
