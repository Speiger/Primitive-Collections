package speiger.src.builder.mappers;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import speiger.src.builder.misc.RegexUtil;

public class ArgumentMapper implements UnaryOperator<String>
{
	Pattern pattern;
	String replacement;
	String argumentBreaker;
	String braces = "()";
	boolean removeBraces;
	
	public ArgumentMapper(String pattern, String replacement, String argumentBreaker)
	{
		this.pattern = Pattern.compile(pattern);
		this.replacement = replacement;
		this.argumentBreaker = argumentBreaker;
	}
	
	public ArgumentMapper setBraceType(String s) 
	{
		if(s.length() != 2) throw new IllegalStateException("Start and End char only");
		braces = s;
		return this;
	}
	
	public ArgumentMapper removeBraces()
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
			do
			{
				String text = RegexUtil.searchUntil(t, matcher.end()-1, braces.charAt(0), braces.charAt(1));
				if(!text.isEmpty())
				{
					RegexUtil.skip(matcher.appendReplacement(buffer, ""), text.length());
					buffer.append(String.format(replacement, (Object[])getString(text).split(argumentBreaker)));
				}
			}
			while(matcher.find());
			matcher.appendTail(buffer);
			return buffer.toString();
		}
		return t;
	}
	
	protected String getString(String s)
	{
		return removeBraces ? s.substring(1, s.length() - 1) : s;
	}
	
}
