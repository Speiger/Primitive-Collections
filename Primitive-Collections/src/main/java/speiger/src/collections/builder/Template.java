package speiger.src.collections.builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class Template
{
	String textFile;
	List<ConditionedSegment> segments;
	
	public Template(String textFile, List<ConditionedSegment> segments)
	{
		this.textFile = textFile;
		this.segments = segments;
	}

	public String build(Set<String> parsePool)
	{
		StringBuilder builder = new StringBuilder(textFile);
		for(int i = 0,offset=0,m=segments.size();i<m;i++)
		{
			offset += segments.get(i).build(parsePool, builder, offset);
		}
		return builder.toString();
	}
	
	public static void main(String...args)
	{
		try
		{
			Template template = parse(new File("./src/main/resources/speiger/assets/collections/templates/List.template").toPath());
			Set<String> parsePool = new HashSet<>();
			parsePool.add("DEPEND");
			parsePool.add("SUB_TEST");
			parsePool.add("TEST_0");
			parsePool.add("TEST_1");
			parsePool.add("TEST_2");
			System.out.println(template.build(parsePool));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Template parse(Path file) throws IOException
	{
		List<ConditionedSegment> segments = new ArrayList<ConditionedSegment>();
		StringJoiner joiner = new StringJoiner("\n");
		List<String> lines = Files.readAllLines(file);
		for(int i = 0;i<lines.size();i++)
		{
			String s = lines.get(i);
			if(s.trim().startsWith("#if"))
			{
				i += ConditionedSegment.parse(s.trim().substring(3).trim(), lines, i, joiner.length(), segments);
				continue;
			}
			joiner.add(s);
		}
		return new Template(joiner.toString(), segments);
	}
	
	public static int parseSegments(String conditionLine, List<String> file, int index)
	{
		return 0;
	}
}
