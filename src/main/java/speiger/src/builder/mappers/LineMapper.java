package speiger.src.builder.mappers;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import speiger.src.builder.misc.RegexUtil;

public class LineMapper implements UnaryOperator<String>
{
	Pattern pattern;

	public LineMapper(String pattern)
	{
		this.pattern = Pattern.compile(pattern, Pattern.LITERAL);
	}

	@Override
	public String apply(String t)
	{
		Matcher matcher = pattern.matcher(t);
		if(matcher.find())
		{
			StringBuffer buffer = new StringBuffer();			
			do
			{
				int start = matcher.end() - 1;
				int[] result = RegexUtil.findFullLine(t, start);
				if(result != null)
				{
					matcher.appendReplacement(buffer, pattern.pattern());
					buffer.setLength(buffer.length() - (start - result[0]));
					RegexUtil.skip(matcher, result[1] - start);
				}
            } while (matcher.find());
            matcher.appendTail(buffer);
            return buffer.toString();
		}
		return t;
	}
	
	
}
