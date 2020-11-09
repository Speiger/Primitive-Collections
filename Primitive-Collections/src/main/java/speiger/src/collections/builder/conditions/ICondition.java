package speiger.src.collections.builder.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ICondition
{
	public static final ICondition ALWAYS_TRUE = T -> true;
	
	public boolean isValid(Set<String> parsePool);
	
	
	public static ICondition parse(String condition)
	{
		String[] elements = condition.split(" ");
		List<ICondition> conditions = new ArrayList<ICondition>();
		for(int i = 0;i<elements.length;i++)
		{
			if(elements[i].equalsIgnoreCase("&&"))
			{
				if(i==elements.length-1)
				{
					continue;
				}
				if(condition.isEmpty())
				{
					conditions.add(new AndCondition(parseSimpleCondition(elements[++i])));
				}
				else
				{
					ICondition con = conditions.get(conditions.size() - 1);
					if(con instanceof AndCondition)
					{
						((AndCondition)con).addCondition(parseSimpleCondition(elements[++i]));
					}
					else
					{
						AndCondition replacement = new AndCondition(con);
						replacement.addCondition(parseSimpleCondition(elements[++i]));
						conditions.set(conditions.size()-1, replacement);
					}
				}
			}
			else if(!elements[i].equalsIgnoreCase("||"))
			{
				conditions.add(parseSimpleCondition(elements[i]));
			}
		}
		switch(conditions.size())
		{
			case 0: return ALWAYS_TRUE;
			case 1: return conditions.get(0);
			default: return new OrCondition(conditions);
		}
	}
	
	static ICondition parseSimpleCondition(String s)
	{
		return s.startsWith("!") ? new FlagCondition(s.substring(1), true) : new FlagCondition(s, false);
	}
}
