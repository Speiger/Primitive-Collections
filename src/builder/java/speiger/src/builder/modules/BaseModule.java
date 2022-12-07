package speiger.src.builder.modules;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import speiger.src.builder.ClassType;
import speiger.src.builder.ModulePackage;
import speiger.src.builder.RequiredType;
import speiger.src.builder.SettingsManager;
import speiger.src.builder.mappers.ArgumentMapper;
import speiger.src.builder.mappers.InjectMapper;
import speiger.src.builder.mappers.LineMapper;
import speiger.src.builder.mappers.SimpleMapper;

@SuppressWarnings("javadoc")
public abstract class BaseModule
{
	SettingsManager manager;
	ModulePackage entry;
	protected ClassType keyType;
	protected ClassType valueType;
	
	public final void setManager(SettingsManager manager) {
		this.manager = manager;
		manager.addModule(this);
	}
	
	public final void init(ModulePackage entry) {
		this.entry = entry;
		keyType = entry.getKeyType();
		valueType = entry.getValueType();
		loadVariables();
		loadClasses();
		loadTestClasses();
		loadFunctions();
		loadRemappers();
		loadBlockades();
		loadFlags();
	}
	
	public final void cleanup() {
		entry = null;
		keyType = null;
		valueType = null;
		manager = null;
	}
	
	protected abstract void loadVariables();
	protected abstract void loadClasses();
	protected abstract void loadTestClasses();
	protected abstract void loadFunctions();
	protected abstract void loadRemappers();
	protected abstract void loadBlockades();
	protected abstract void loadFlags();
	
	public abstract String getModuleName();
	public boolean isBiModule() { return false; }
	public Set<String> getModuleKeys(ClassType keyType, ClassType valueType) { return Collections.emptySet(); }
	public boolean isModuleValid(ClassType keyType, ClassType valueType) { return true; }
	
	protected boolean isModuleEnabled() {
		return manager == null || manager.isModuleEnabled(this, keyType, valueType);
	}
	
	protected boolean isModuleEnabled(String name) {
		return manager == null || manager.isModuleEnabled(this, keyType, valueType, name);
	}
	
	protected boolean isDependencyLoaded(BaseModule module) {
		return isDependencyLoaded(module, true);
	}
	
	protected boolean isDependencyLoaded(BaseModule module, boolean key) {
		return manager == null || (module.isBiModule() ? manager.isModuleEnabled(module, keyType, valueType) : (key ? manager.isModuleEnabled(module, keyType, keyType) : manager.isModuleEnabled(module, valueType, valueType))); 
	}
	
	public boolean areDependenciesLoaded() {
		return true;
	}
	
	protected void addFlag(String name) {
		entry.addFlag(name);
	}
	
	protected void addKeyFlag(String name) {
		entry.addFlag(name);
		entry.addGlobalFlag(keyType.getCapType()+"_"+name);
	}
	
	protected void addBiRequirement(String fileName) {
		entry.addRequirement(fileName, RequiredType.BI_CLASS);
		entry.addSplitter(fileName, "%1$s2%2$s");
	}
	
	protected void addEnumRequirement(String fileName) {
		entry.addRequirement(fileName, RequiredType.ENUM);
		entry.addSplitter(fileName, "%2$s");

	}
	
	protected void addBiRequirement(String fileName, String splitter) {
		entry.addRequirement(fileName, RequiredType.BI_CLASS);
		entry.addSplitter(fileName, "%1$s"+splitter+"%2$s");
	}
	
	protected void addRequirement(String fileName, String splitter, RequiredType type) {
		entry.addRequirement(fileName, type);
		entry.addSplitter(fileName, splitter);
	}
	
	protected void addRemapper(String fileName, String actualName) {
		entry.addRemapper(fileName, actualName);
	}
	
	protected void addBlockedFiles(String... name) {
		entry.addBlockedFiles(name);
	}
	
	protected void addBlockedFilter(Predicate<String> filter) {
		entry.addBlockedFilter(filter);
	}
	
	protected void addClassMapper(String pattern, String replacement) {
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, valueType.getFileType()+replacement));
		entry.addMapper(new SimpleMapper(pattern, pattern, keyType.getFileType()+replacement));
	}
	
	protected void addBiClassMapper(String pattern, String replacement, String splitter) {
		entry.addMapper(new SimpleMapper("KEY_"+pattern, "KEY_"+pattern, keyType.getFileType()+splitter+keyType.getFileType()+replacement));
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, valueType.getFileType()+splitter+valueType.getFileType()+replacement));
		entry.addMapper(new SimpleMapper(pattern, pattern, keyType.getFileType()+splitter+valueType.getFileType()+replacement));
	}
	
	protected void addAbstractMapper(String pattern, String replacement) {
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, String.format(replacement, valueType.getFileType())));
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, keyType.getFileType())));		
	}
	
	protected void addAbstractBiMapper(String pattern, String replacement, String splitter) {
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, keyType.getFileType()+splitter+valueType.getFileType())));
	}
	
	protected void addFunctionMapper(String pattern, String replacement) {
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, replacement+valueType.getNonFileType()));
		entry.addMapper(new SimpleMapper(pattern, pattern, replacement+keyType.getNonFileType()));
	}
	
	protected void addFunctionValueMapper(String pattern, String replacement) {
		entry.addMapper(new SimpleMapper(pattern, pattern, replacement+valueType.getNonFileType()));
	}
	
	protected void addFunctionMappers(String pattern, String replacement) {
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, String.format(replacement, valueType.getNonFileType())));		
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, keyType.getNonFileType())));		
	}
	
	protected void addFunctionValueMappers(String pattern, String replacement) {
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, valueType.getNonFileType())));		
	}
	
	protected void addSimpleMapper(String pattern, String replacement) {
		entry.addMapper(new SimpleMapper(pattern, pattern, replacement));
	}
	
	protected void addAnnontion(String pattern, String value) {
		if(keyType == ClassType.OBJECT) entry.addMapper(new LineMapper(pattern, pattern));
		else entry.addMapper(new SimpleMapper(pattern, pattern, value));
	}
	
	protected void addValueAnnontion(String pattern, String value) {
		if(valueType == ClassType.OBJECT) entry.addMapper(new LineMapper(pattern, pattern));
		else entry.addMapper(new SimpleMapper(pattern, pattern, value));
	}
	
	protected void addComment(String pattern, String value) {
		if(keyType == ClassType.OBJECT) entry.addMapper(new InjectMapper(pattern, pattern, value).removeBraces());
		else entry.addMapper(new LineMapper(pattern, pattern));
	}
	
	protected void addValueComment(String pattern, String value) {
		if(valueType == ClassType.OBJECT) entry.addMapper(new InjectMapper(pattern, pattern, value).removeBraces());
		else entry.addMapper(new LineMapper(pattern, pattern));
	}
	
	protected InjectMapper addInjectMapper(String pattern, String replacement) {
		InjectMapper mapper = new InjectMapper(pattern, pattern, replacement);
		entry.addMapper(mapper);
		return mapper;
	}
	
	protected ArgumentMapper addArgumentMapper(String pattern, String replacement) {
		return addArgumentMapper(pattern, replacement, ", ");
	}
	
	protected ArgumentMapper addArgumentMapper(String pattern, String replacement, String splitter) {
		ArgumentMapper mapper = new ArgumentMapper(pattern, pattern, replacement, splitter);
		entry.addMapper(mapper);
		return mapper;
	}
}
