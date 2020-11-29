package speiger.src.builder.misc;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

public class RegexUtil
{
	static Field LAST_POS;
	
	public static Matcher skip(Matcher matcher, int amount)
	{
		try
		{
			LAST_POS.setInt(matcher, LAST_POS.getInt(matcher) + amount);
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		return matcher;
	}
	
	public static String searchUntil(String text, int startIndex, char increase, char decrease)
	{
		if(text.charAt(startIndex + 1) != increase)
		{
			return "";
		}
		int inc = 0;
		StringBuilder builder = new StringBuilder();
		for(int i = startIndex + 1;i<text.length();i++)
		{
			char current = text.charAt(i);
			if(current == '\\' && i < text.length() - 1 && text.charAt(i+1) == 'n')
			{
				return "";
			}
			else if(current == increase)
			{
				inc++;
			}
			else if(current == decrease)
			{
				inc--;
				if(inc <= 0)
				{
					return builder.append(decrease).toString();
				}
			}
			builder.append(current);
		}
		return "";
	}
	
	public static int[] findFullLine(String s, int startIndex)
	{
		int offset = s.indexOf("\n", startIndex);
		if(offset == -1) return null;
		int start = s.lastIndexOf("\n", startIndex);
		if(start == -1) return null;
		return new int[]{start, offset};
	}
	
	static
	{
		try
		{
			Field field = Matcher.class.getDeclaredField("lastAppendPosition");
			field.setAccessible(true);
			LAST_POS = field;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
