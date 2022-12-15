package speiger.src.builder.modules;

import speiger.src.builder.ClassType;
import speiger.src.builder.RequiredType;

@SuppressWarnings("javadoc")
public class FunctionModule extends BaseModule
{
	public static final BaseModule INSTANCE = new FunctionModule();
	
	@Override
	public String getModuleName() { return "Function"; }
	@Override
	public boolean isBiModule() { return true; }
	@Override
	protected void loadVariables() {}
	@Override
	protected void loadFlags() {}
	@Override
	protected void loadTestClasses() {}
	
	@Override
	protected void loadBlockades()
	{
		if(keyType.isObject()) addBlockedFiles("Consumer", "Comparator");
	}
	
	@Override
	protected void loadRemappers()
	{
		addBiRequirement("BiConsumer", "");
		addBiRequirement("UnaryOperator", "");
		if(valueType == ClassType.BOOLEAN) {
			addRequirement("Function", "%1$s", RequiredType.BI_CLASS);
			addRemapper("Function", (keyType.isObject() ? "" : "%s")+"Predicate");
		}
		else if(keyType.isObject() && !valueType.isObject()) {
			addRequirement("Function", "%2$s", RequiredType.BI_CLASS);
			addRemapper("Function", "To%sFunction");			
		}
		else if(keyType == valueType) {
			addRequirement("Function", "%1$s", RequiredType.BI_CLASS);
			addRemapper("Function", (keyType.isObject() ? "" : "%s")+"UnaryOperator");
		}
		else if(valueType.isObject()) {
			addRequirement("Function", "%1$s", RequiredType.BI_CLASS);
			addRemapper("Function", "%sFunction");
		}
		else addBiRequirement("Function");
		addRemapper("BiConsumer", "%sConsumer");
	}
	
	@Override
	protected void loadFunctions()
	{
		addSimpleMapper("APPLY", keyType.getApply(valueType));
		addSimpleMapper("SUPPLY_GET", keyType.isObject() ? "get" : "getAs"+keyType.getCustomJDKType().getNonFileType());
		addSimpleMapper("VALUE_SUPPLY_GET", valueType.isObject() ? "get" : "getAs"+valueType.getCustomJDKType().getNonFileType());
	}
	
	@Override
	protected void loadClasses()
	{
		//Interfaces
		addBiClassMapper("BI_CONSUMER", "Consumer", "");
		addClassMapper("BI_TO_OBJECT_CONSUMER", "ObjectConsumer");
		addAbstractMapper("BI_FROM_OBJECT_CONSUMER", "Object%sConsumer");
		addAbstractMapper("BI_FROM_INT_CONSUMER", "Int%sConsumer");
		if(keyType.isObject()) {
			addSimpleMapper("TO_OBJECT_FUNCTION", keyType.getNonFileType()+"UnaryOperator");
			addSimpleMapper("VALUE_TO_OBJECT_FUNCTION", valueType.isObject() ? "UnaryOperator" : valueType.getFileType()+"Function");
		}
		else {
			addSimpleMapper("TO_OBJECT_FUNCTION", keyType.getNonFileType()+"Function");
			addSimpleMapper("VALUE_TO_OBJECT_FUNCTION", valueType.isObject() ? "UnaryOperator" : valueType.getFileType()+"Function");
		}
		if(valueType == ClassType.BOOLEAN) addFunctionMappers("FUNCTION", "%sPredicate");
		else if(keyType.isObject() && !valueType.isObject()) addFunctionValueMappers("FUNCTION", "To%sFunction");
		else if(keyType == valueType) addFunctionMappers("FUNCTION", "%sUnaryOperator");
		else if(valueType.isObject()) addFunctionMappers("FUNCTION", "%sFunction");
		else addBiClassMapper("FUNCTION", "Function", "2");
		
		addFunctionMappers("PREDICATE", "%sPredicate");
		addClassMapper("SUPPLIER", "Supplier");
		addAbstractMapper("SINGLE_UNARY_OPERATOR", "%1$s%1$sUnaryOperator");
		addBiClassMapper("UNARY_OPERATOR", "UnaryOperator", "");
		if(keyType.isObject())
		{
			if(!valueType.isObject()) addSimpleMapper("VALUE_CONSUMER", valueType.getFileType()+"Consumer");
			else addSimpleMapper("VALUE_CONSUMER", "Consumer");
			addSimpleMapper("CONSUMER", "Consumer");
			addSimpleMapper("IARRAY", "IObjectArray");
		}
		else
		{
			if(valueType.isObject()) 
			{
				addSimpleMapper("VALUE_CONSUMER", "Consumer");
				addSimpleMapper("CONSUMER", keyType.getFileType()+"Consumer");
			}
			else addClassMapper("CONSUMER", "Consumer");
			addFunctionMappers("IARRAY", "I%sArray");
		}
		addSimpleMapper("VALUE_COMPARATOR", valueType.isObject() ? "Comparator" : String.format("%sComparator", valueType.getNonFileType()));
		addSimpleMapper("COMPARATOR", keyType.isObject() ? "Comparator" : String.format("%sComparator", keyType.getNonFileType()));
	}
}