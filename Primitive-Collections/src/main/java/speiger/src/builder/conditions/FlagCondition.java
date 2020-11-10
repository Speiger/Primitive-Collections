package speiger.src.builder.conditions;

import java.util.Set;

public class FlagCondition implements ICondition
{
	String flag;
	boolean inverted;
	
	public FlagCondition(String flag, boolean inverted)
	{
		this.flag = flag;
		this.inverted = inverted;
	}

	@Override
	public boolean isValid(Set<String> parsePool)
	{
		return parsePool.contains(flag) != inverted;
	}
}
