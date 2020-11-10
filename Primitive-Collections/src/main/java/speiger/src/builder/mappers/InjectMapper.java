package speiger.src.builder.mappers;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InjectMapper implements UnaryOperator<String>
{
	Pattern pattern;
	String replacement;
	boolean removeBraces;
	
	public InjectMapper(String pattern, String replacement)
	{
		this.pattern = Pattern.compile(pattern);
		this.replacement = replacement;
	}
	
	public InjectMapper removeBraces()
	{
		removeBraces = true;
		return this;
	}
	
	@Override
	public String apply(String t)
	{
		Matcher matcher = pattern.matcher(t);
		if(matcher.find())
		{
			StringBuffer buffer = new StringBuffer();			
            do { matcher.appendReplacement(buffer, String.format(replacement, getString(matcher.group(1))));
            } while (matcher.find());
            matcher.appendTail(buffer);
            return buffer.toString();
		}
		return t;
	}
	
	private String getString(String s)
	{
		return removeBraces ? s.substring(1, s.length() - 1) : s;
	}
}
