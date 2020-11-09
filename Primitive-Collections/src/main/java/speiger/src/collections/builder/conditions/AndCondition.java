package speiger.src.collections.builder.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AndCondition implements ICondition
{
	List<ICondition> conditions = new ArrayList<>();
	
	public AndCondition(ICondition base)
	{
		conditions.add(base);
	}
	
	public void addCondition(ICondition e)
	{
		conditions.add(e);
	}
	
	@Override
	public boolean isValid(Set<String> parsePool)
	{
		for(int i = 0,m=conditions.size();i<m;i++)
		{
			if(!conditions.get(i).isValid(parsePool))
			{
				return false;
			}
		}
		return true;
	}
}
