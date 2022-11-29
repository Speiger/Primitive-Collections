package speiger.src.builder.modules;

@SuppressWarnings("javadoc")
public class FunctionModule extends BaseModule
{
	@Override
	protected void loadVariables()
	{
		loadFunctions();
		loadClasses();
		loadRemappers();
		loadBlockades();
	}
	
	@Override
	protected void loadFlags()
	{
		
	}
	
	private void loadBlockades()
	{
		if(keyType.isObject()) addBlockedFiles("Consumer", "Comparator");
	}
	
	private void loadRemappers()
	{
		addBiRequirement("BiConsumer", "");
		addBiRequirement("UnaryOperator", "");
		addBiRequirement("Function");
		addRemapper("BiConsumer", "%sConsumer");
	}
	
	private void loadFunctions()
	{
		addSimpleMapper("VALUE_TEST_VALUE", valueType.isObject() ? "getBoolean" : "get");
		addSimpleMapper("TEST_VALUE", keyType.isObject() ? "getBoolean" : "get");
	}
	
	private void loadClasses()
	{
		//Interfaces
		addBiClassMapper("BI_CONSUMER", "Consumer", "");
		addClassMapper("BI_TO_OBJECT_CONSUMER", "ObjectConsumer");
		addAbstractMapper("BI_FROM_OBJECT_CONSUMER", "Object%sConsumer");
		addClassMapper("TO_OBJECT_FUNCTION", "2ObjectFunction");
		addBiClassMapper("FUNCTION", "Function", "2");
		addClassMapper("PREDICATE", "2BooleanFunction");
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