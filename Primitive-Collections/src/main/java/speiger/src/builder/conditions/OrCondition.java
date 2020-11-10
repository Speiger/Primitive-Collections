package speiger.src.builder.conditions;

import java.util.List;
import java.util.Set;

public class OrCondition implements ICondition
{
	List<ICondition> conditions;
	
	public OrCondition(List<ICondition> conditions)
	{
		this.conditions = conditions;
	}

	@Override
	public boolean isValid(Set<String> parsePool)
	{
		for(int i = 0,m=conditions.size();i<m;i++)
		{
			if(conditions.get(i).isValid(parsePool))
			{
				return true;
			}
		}
		return false;
	}
}
