package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class PairModule extends BaseModule
{
	public static final BaseModule INSTANCE = new PairModule();
	@Override
	public String getModuleName() { return "Pair"; }
	@Override
	public boolean isBiModule() { return true; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadBlockades() {}
	@Override
	protected void loadFlags() {}
	@Override
	protected void loadFunctions() {}
	@Override
	protected void loadTestClasses() {}
	
	@Override
	protected void loadRemappers()
	{
		//Main Classes
		addBiRequirement("Pair", "");
		addBiRequirement("MutablePair", "");
		addBiRequirement("ImmutablePair", "");
		
		//Test Classes
		addBiRequirement("PairTester", "");
	}
	
	@Override
	protected void loadClasses()
	{
		//Implementations
		addBiClassMapper("IMMUTABLE_PAIR", "ImmutablePair", "");
		addBiClassMapper("MUTABLE_PAIR", "MutablePair", "");
		
		//Interfaces
		addBiClassMapper("PAIR", "Pair", "");
	}
}