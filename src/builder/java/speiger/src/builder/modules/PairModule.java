package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class PairModule extends BaseModule
{
	@Override
	protected void loadVariables()
	{
		loadClasses();
		loadRemappers();
	}
	
	@Override
	protected void loadFlags()
	{
		
	}
	
	private void loadRemappers()
	{
		addBiRequirement("Pair", "");
		addBiRequirement("MutablePair", "");
		addBiRequirement("ImmutablePair", "");
	}
	
	private void loadClasses()
	{
		//Implementations
		addBiClassMapper("IMMUTABLE_PAIR", "ImmutablePair", "");
		addBiClassMapper("MUTABLE_PAIR", "MutablePair", "");
		
		//Interfaces
		addBiClassMapper("PAIR", "Pair", "");
	}
}