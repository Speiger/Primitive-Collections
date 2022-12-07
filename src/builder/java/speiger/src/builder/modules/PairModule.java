package speiger.src.builder.modules;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import speiger.src.builder.ClassType;

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
	protected void loadFunctions() {}
	@Override
	protected void loadTestClasses() {}
	@Override
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType) { return new TreeSet<>(Arrays.asList("Mutable", "Immutable")); }
	
	@Override
	protected void loadFlags() {
		if(isModuleEnabled()) addFlag("PAIR_MODULE");
		if(isModuleEnabled("Mutable")) addFlag("MUTABLE_PAIR");
		if(isModuleEnabled("Immutable")) addFlag("IMMUTABLE_PAIR");
	}
	
	@Override
	protected void loadBlockades() {
		if(!isModuleEnabled()) addBlockedFiles("Pair");
		if(!isModuleEnabled("Mutable")) addBlockedFiles("MutablePair");
		if(!isModuleEnabled("Immutable")) addBlockedFiles("ImmutablePair");
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