package speiger.src.builder.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import speiger.src.builder.conditions.ICondition;
import speiger.src.builder.misc.Tuple;

public class ConditionedSegment
{
	static final Pattern AND = Pattern.compile("(&&)");
	static final Pattern OR = Pattern.compile("(||)");
	
	int index;
	List<Tuple<ICondition, Segment>> segments = new ArrayList<>();
	
	public ConditionedSegment(int index)
	{
		this.index = index;
	}
	
	public void addSegment(ICondition condition, Segment segment)
	{
		segments.add(new Tuple<>(condition, segment));
	}
	
	public int build(Set<String> parsePool, StringBuilder builder, int baseIndex)
	{
		baseIndex += index;
		int length = builder.length();
		for(int i = 0,m=segments.size();i<m;i++)
		{
			Tuple<ICondition, Segment> entry = segments.get(i);
			if(entry.getKey().isValid(parsePool))
			{
				entry.getValue().build(parsePool, builder, baseIndex);
				break;
			}
		}
		return builder.length() - length;
	}
	
	public static int parse(String currentLine, List<String> lines, int currentIndex, int startIndex, List<ConditionedSegment> segments) throws IllegalStateException
	{
		ConditionedSegment segment = new ConditionedSegment(startIndex);
		ICondition condition = ICondition.parse(currentLine);
		List<ConditionedSegment> childSegments = new ArrayList<>();
		StringJoiner segmentText = new StringJoiner("\n", "\n", "");
		for(int i = currentIndex+1;i<lines.size();i++)
		{
			String s = lines.get(i);
			String trimmed = s.trim();
			if(trimmed.startsWith("#"))
			{
				if(trimmed.startsWith("#else if"))
				{
					segment.addSegment(condition, new Segment(segmentText.toString(), childSegments));
					condition = ICondition.parse(trimmed.substring(8).trim());
					childSegments = new ArrayList<>();
					segmentText = new StringJoiner("\n", "\n", "");
				}
				else if(trimmed.startsWith("#else"))
				{
					segment.addSegment(condition, new Segment(segmentText.toString(), childSegments));
					condition = ICondition.ALWAYS_TRUE;
					childSegments = new ArrayList<>();
					segmentText = new StringJoiner("\n", "\n", "");
				}
				else if(trimmed.startsWith("#endif"))
				{
					segment.addSegment(condition, new Segment(segmentText.toString(), childSegments));
					segments.add(segment);
					return i - currentIndex;
				}
				else if(trimmed.startsWith("#if"))
				{
					i += parse(trimmed.substring(3).trim(), lines, i, segmentText.length(), childSegments);
				}
				continue;
			}
			segmentText.add(s);
		}
		throw new IllegalStateException("Unclosed #If found!");
	}
	
}
