package speiger.src.builder.mappers;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SimpleMapper implements UnaryOperator<String>
{
	Pattern pattern;
	String replacement;

	public SimpleMapper(String pattern, String replacement)
	{
		this.pattern = Pattern.compile(pattern);
		this.replacement = replacement;
	}
	
	@Override
	public String apply(String t)
	{
		return pattern.matcher(t).replaceAll(replacement);
	}
}
