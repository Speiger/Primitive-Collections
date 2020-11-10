package speiger.src.builder.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Segment
{
	String text;
	List<ConditionedSegment> segments = new ArrayList<>();
	
	public Segment(String text, List<ConditionedSegment> segments)
	{
		this.text = text;
		this.segments = segments;
	}
	
	public int build(Set<String> parsePool, StringBuilder builder, int index)
	{
		int length = builder.length();
		builder.insert(index, text);
		for(int i = 0,offset=0,m=segments.size();i<m;i++)
		{
			offset += segments.get(i).build(parsePool, builder, index+offset);
		}
		return builder.length() - length;
	}
}
