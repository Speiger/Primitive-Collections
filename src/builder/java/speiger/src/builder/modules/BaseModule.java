package speiger.src.builder.modules;

import java.util.function.Predicate;

import speiger.src.builder.ClassType;
import speiger.src.builder.mappers.ArgumentMapper;
import speiger.src.builder.mappers.InjectMapper;
import speiger.src.builder.mappers.LineMapper;
import speiger.src.builder.mappers.SimpleMapper;

@SuppressWarnings("javadoc")
public abstract class BaseModule
{
	ModulePackage entry;
	protected ClassType keyType;
	protected ClassType valueType;
	
	public final void init(ModulePackage entry)
	{
		this.entry = entry;
		keyType = entry.getKeyType();
		valueType = entry.getValueType();
		loadVariables();
		loadFlags();
	}
	
	public final void cleanup() {
		entry = null;
		keyType = null;
		valueType = null;
	}
	
	protected abstract void loadVariables();
	protected abstract void loadFlags();
	
	protected void addFlag(String name)
	{
		entry.addFlag(name);
	}
	
	protected void addBiRequirement(String fileName) {
		entry.addRequirement(fileName, RequiredType.BI_CLASS);
		entry.addSplitter(fileName, "%1$s2%2$s");
	}
	
	protected void addBiRequirement(String fileName, String splitter) {
		entry.addRequirement(fileName, RequiredType.BI_CLASS);
		entry.addSplitter(fileName, "%1$s"+splitter+"%2$s");
	}
	
	protected void addRequirement(String fileName, String splitter, RequiredType type) {
		entry.addRequirement(fileName, type);
		entry.addSplitter(fileName, splitter);
	}
	
	protected void addBlockedFile(String name)
	{
		entry.addBlockedFile(name);
	}
	
	protected void addBlockedFilter(Predicate<String> filter)
	{
		entry.addBlockedFilter(filter);
	}
	
	protected void addClassMapper(String pattern, String replacement)
	{
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, valueType.getFileType()+replacement));
		entry.addMapper(new SimpleMapper(pattern, pattern, keyType.getFileType()+replacement));
	}
	
	protected void addBiClassMapper(String pattern, String replacement, String splitter)
	{
		entry.addMapper(new SimpleMapper("KEY_"+pattern, "KEY_"+pattern, keyType.getFileType()+splitter+keyType.getFileType()+replacement));
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, valueType.getFileType()+splitter+valueType.getFileType()+replacement));
		entry.addMapper(new SimpleMapper(pattern, pattern, keyType.getFileType()+splitter+valueType.getFileType()+replacement));
	}
	
	protected void addAbstractMapper(String pattern, String replacement)
	{
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, String.format(replacement, valueType.getFileType())));
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, keyType.getFileType())));		
	}
	
	protected void addAbstractBiMapper(String pattern, String replacement, String splitter) 
	{
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, keyType.getFileType()+splitter+valueType.getFileType())));
	}
	
	protected void addFunctionMapper(String pattern, String replacement)
	{
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, replacement+valueType.getNonFileType()));
		entry.addMapper(new SimpleMapper(pattern, pattern, replacement+keyType.getNonFileType()));
	}
	
	protected void addFunctionValueMapper(String pattern, String replacement)
	{
		entry.addMapper(new SimpleMapper(pattern, pattern, replacement+valueType.getNonFileType()));
	}
	
	protected void addFunctionMappers(String pattern, String replacement)
	{
		entry.addMapper(new SimpleMapper("VALUE_"+pattern, "VALUE_"+pattern, String.format(replacement, valueType.getNonFileType())));		
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, keyType.getNonFileType())));		
	}
	
	protected void addFunctionValueMappers(String pattern, String replacement)
	{
		entry.addMapper(new SimpleMapper(pattern, pattern, String.format(replacement, valueType.getNonFileType())));		
	}
	
	protected void addSimpleMapper(String pattern, String replacement)
	{
		entry.addMapper(new SimpleMapper(pattern, pattern, replacement));
	}
	
	protected void addAnnontion(String pattern, String value)
	{
		if(keyType == ClassType.OBJECT) entry.addMapper(new LineMapper(pattern, pattern));
		else entry.addMapper(new SimpleMapper(pattern, pattern, value));
	}
	
	protected void addValueAnnontion(String pattern, String value)
	{
		if(valueType == ClassType.OBJECT) entry.addMapper(new LineMapper(pattern, pattern));
		else entry.addMapper(new SimpleMapper(pattern, pattern, value));
	}
	
	protected void addComment(String pattern, String value)
	{
		if(keyType == ClassType.OBJECT) entry.addMapper(new InjectMapper(pattern, pattern, value).removeBraces());
		else entry.addMapper(new LineMapper(pattern, pattern));
	}
	
	protected void addValueComment(String pattern, String value)
	{
		if(valueType == ClassType.OBJECT) entry.addMapper(new InjectMapper(pattern, pattern, value).removeBraces());
		else entry.addMapper(new LineMapper(pattern, pattern));
	}
	
	protected InjectMapper addInjectMapper(String pattern, String replacement)
	{
		InjectMapper mapper = new InjectMapper(pattern, pattern, replacement);
		entry.addMapper(mapper);
		return mapper;
	}
	
	protected ArgumentMapper addArgumentMapper(String pattern, String replacement)
	{
		return addArgumentMapper(pattern, replacement, ", ");
	}
	
	protected ArgumentMapper addArgumentMapper(String pattern, String replacement, String splitter)
	{
		ArgumentMapper mapper = new ArgumentMapper(pattern, pattern, replacement, splitter);
		entry.addMapper(mapper);
		return mapper;
	}
}
