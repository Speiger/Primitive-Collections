package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.List;

import speiger.src.builder.ClassType;
import speiger.src.builder.dependency.DependencyFunction;
import speiger.src.builder.dependency.DependencyModule;
import speiger.src.builder.dependency.DependencyModule.BiTypeModule;
import speiger.src.builder.dependency.IDependency;

@SuppressWarnings("javadoc")
public class PairModule extends BaseModule
{
	public static final BaseModule INSTANCE = new PairModule();
	public static final DependencyModule MODULE = new BiTypeModule(INSTANCE);
	public static final DependencyFunction IMMUTABLE = MODULE.createFunction("Immutable");
	public static final DependencyFunction MUTABLE = MODULE.createFunction("Mutable");
	
	@Override
	public String getModuleName() { return "Pair"; }
	@Override
	public boolean isBiModule() { return true; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFunctions() {}
	@Override
	protected void loadTestClasses() {}
	@Override
	public List<IDependency> getDependencies(ClassType keyType, ClassType valueType) { return Arrays.asList(MODULE, IMMUTABLE, MUTABLE); }
	
	@Override
	protected void loadFlags() {
		if(MODULE.isEnabled()) addFlag("PAIR_MODULE");
		if(MUTABLE.isEnabled()) addFlag("MUTABLE_PAIR");
		if(IMMUTABLE.isEnabled()) addFlag("IMMUTABLE_PAIR");
	}
	
	@Override
	protected void loadBlockades() {
		if(!MODULE.isEnabled()) addBlockedFiles("Pair");
		if(!MUTABLE.isEnabled()) addBlockedFiles("MutablePair");
		if(!IMMUTABLE.isEnabled()) addBlockedFiles("ImmutablePair");
	}
	
	@Override
	protected void loadRemappers() {
		//Main Classes
		addBiRequirement("Pair", "");
		addBiRequirement("MutablePair", "");
		addBiRequirement("ImmutablePair", "");
		
		//Test Classes
		addBiRequirement("PairTester", "");
	}
	
	@Override
	protected void loadClasses() {
		//Implementations
		addBiClassMapper("IMMUTABLE_PAIR", "ImmutablePair", "");
		addBiClassMapper("MUTABLE_PAIR", "MutablePair", "");
		
		//Interfaces
		addBiClassMapper("PAIR", "Pair", "");
	}
}