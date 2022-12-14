package speiger.src.builder.modules;

import speiger.src.builder.ClassType;

@SuppressWarnings("javadoc")
public class JavaModule extends BaseModule
{
	public static final BaseModule INSTANCE = new JavaModule();
	
	@Override
	public String getModuleName() { return "Base"; }
	@Override
	protected void loadVariables()
	{
		createHelperVars(keyType, false, "KEY");
		createHelperVars(valueType, true, "VALUE");
		loadBaseVariables();
	}
	
	@Override
	protected void loadFlags()
	{
		addFlag("TYPE_"+keyType.getCapType());
		addFlag("VALUE_"+valueType.getCapType());
		if(keyType == valueType) addFlag("SAME_TYPE");
		if(keyType.hasFunction(valueType)) addFlag("JDK_FUNCTION");
		if(!keyType.needsCustomJDKType()) addFlag("JDK_TYPE");
		if(!keyType.isPrimitiveBlocking()) addFlag("PRIMITIVES");
		if(!valueType.isPrimitiveBlocking()) addFlag("VALUE_PRIMITIVES");
		if(!valueType.needsCustomJDKType()) addFlag("JDK_VALUE");
	}
	
	@Override
	protected void loadRemappers() {}
	@Override
	protected void loadBlockades() {}
	
	@Override
	protected void loadFunctions()
	{
		addSimpleMapper("APPLY_KEY_VALUE", keyType.isObject() ? "apply" : "applyAs"+keyType.getNonFileType());
		addSimpleMapper("APPLY_VALUE", valueType.isObject() ? "apply" : "applyAs"+valueType.getNonFileType());
		addSimpleMapper("APPLY_CAST", "applyAs"+keyType.getCustomJDKType().getNonFileType());
		
		//Shared by Maps and Pairs so moved to java.
		addFunctionMappers("ENTRY_KEY", "get%sKey");
		addFunctionValueMappers("ENTRY_VALUE", "get%sValue");
		addFunctionMappers("KEY_ENTRY", "set%sKey");
		addFunctionValueMappers("VALUE_ENTRY", "set%sValue");
	}
	
	@Override
	protected void loadClasses()
	{
		addSimpleMapper("JAVA_PREDICATE", keyType.isPrimitiveBlocking() ? "" : keyType.getCustomJDKType().getFileType()+"Predicate");
		addSimpleMapper("JAVA_CONSUMER", keyType.isPrimitiveBlocking() ? "" : "java.util.function."+keyType.getCustomJDKType().getFileType()+"Consumer");
		addSimpleMapper("JAVA_SUPPLIER", keyType.isPrimitiveBlocking() ? "" : "java.util.function."+keyType.getCustomJDKType().getFileType()+"Supplier");
		addSimpleMapper("JAVA_FUNCTION", keyType.getFunctionClass(valueType));
		addSimpleMapper("JAVA_BINARY_OPERATOR", keyType == ClassType.BOOLEAN ? "" : (keyType.isObject() ? "java.util.function.BinaryOperator" : "java.util.function."+keyType.getCustomJDKType().getFileType()+"BinaryOperator"));
		addSimpleMapper("JAVA_UNARY_OPERATOR", keyType.isObject() ? "BinaryOperator" : keyType == ClassType.BOOLEAN ? "" : keyType.getCustomJDKType().getFileType()+"UnaryOperator");
		addSimpleMapper("JAVA_SPLIT_ITERATOR", keyType.isPrimitiveBlocking() ? "Spliterator" : "Of"+keyType.getCustomJDKType().getFileType());
		addSimpleMapper("JAVA_STREAM", keyType.isPrimitiveBlocking() ? "" : keyType.getCustomJDKType().getFileType()+"Stream");
		addSimpleMapper("JAVA_BUFFER", keyType.getFileType()+"Buffer");
	}
	
	@Override
	protected void loadTestClasses()
	{
		addClassMapper("HELPERS", "Helpers");
		addClassMapper("SAMPLE_ELEMENTS", "Samples");
	}
	
	private void loadBaseVariables()
	{
		addSimpleMapper("VALUE_PACKAGE", valueType.getPathType());
		addSimpleMapper("PACKAGE", keyType.getPathType());
		addSimpleMapper("CLASS_TYPE", keyType.getClassType());
		addSimpleMapper("CLASS_VALUE_TYPE", valueType.getClassValueType());
		addSimpleMapper("KEY_TYPE", keyType.getKeyType());
		addSimpleMapper("KEY_OBJECT_TYPE", keyType.isObject() ? "Object" : keyType.getKeyType());
		addSimpleMapper("KEY_STRING_TYPE", keyType.isObject() ? "String" : keyType.getKeyType());
		addSimpleMapper("KEY_SPECIAL_TYPE", keyType.isObject() ? "E" : keyType.getKeyType());
		addSimpleMapper("CLASS_OBJECT_TYPE", keyType.getClassType());
		addSimpleMapper("CLASS_OBJECT_VALUE_TYPE", valueType.getClassValueType());
		addSimpleMapper("CLASS_STRING_TYPE", keyType.isObject() ? "String" : keyType.getClassType());
		addSimpleMapper("CLASS_STRING_VALUE_TYPE", valueType.isObject() ? "String" : valueType.getClassValueType());
		addSimpleMapper("VALUE_TYPE", valueType.getValueType());
		addSimpleMapper("VALUE_OBJECT_TYPE", valueType.isObject() ? "Object" : valueType.getValueType());
		addSimpleMapper("VALUE_STRING_TYPE", valueType.isObject() ? "String" : valueType.getValueType());
		addSimpleMapper("VALUE_SPECIAL_TYPE", valueType.isObject() ? "E" : valueType.getKeyType());
		addSimpleMapper("KEY_JAVA_TYPE", keyType.getCustomJDKType().getKeyType());
		addSimpleMapper("VALUE_JAVA_TYPE", keyType.getCustomJDKType().getKeyType());
		
		addSimpleMapper("EMPTY_KEY_VALUE", keyType.getEmptyValue());
		addSimpleMapper("EMPTY_VALUE", valueType.getEmptyValue());
		
		addSimpleMapper("INVALID_KEY_VALUE", keyType.getInvalidValue());
		addSimpleMapper("INVALID_VALUE", valueType.getInvalidValue());
		
		addSimpleMapper(" KEY_STRING_GENERIC_TYPE", keyType.isObject() ? "<String>" : "");
		addSimpleMapper(" VALUE_STRING_GENERIC_TYPE", valueType.isObject() ? "<String>" : "");
		addSimpleMapper(" KEY_VALUE_STRING_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<String, String>" : "<String>") : (valueType.isObject() ? "<String>" : ""));
		
		addSimpleMapper(" KEY_SAME_GENERIC_TYPE", keyType.isObject() ? "<T, T>" : "");
		addSimpleMapper(" VALUE_SAME_GENERIC_TYPE", keyType.isObject() ? "<V, V>" : "");
		
		addSimpleMapper(" KEY_GENERIC_TYPE", keyType.isObject() ? "<"+keyType.getKeyType()+">" : "");
		addSimpleMapper(" KEY_KEY_GENERIC_TYPE", keyType.isObject() ? "<"+keyType.getKeyType()+", "+keyType.getKeyType()+">" : "");
		addSimpleMapper(" KEY_CLASS_GENERIC_TYPE", keyType.isObject() ? "<"+keyType.getClassType()+">" : "");

		
		addSimpleMapper(" VALUE_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+">" : "");
		addSimpleMapper(" VALUE_VALUE_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+", "+valueType.getValueType()+">" : "");
		addSimpleMapper(" VALUE_CLASS_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getClassValueType()+">" : "");
		
		addSimpleMapper(" KEY_VALUE_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<"+keyType.getKeyType()+", "+valueType.getValueType()+">" : "<"+keyType.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" KEY_VALUE_VALUE_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<"+keyType.getKeyType()+", "+valueType.getValueType()+", "+valueType.getValueType()+">" : "<"+keyType.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+", "+valueType.getValueType()+">" : ""));
		addInjectMapper(" KEY_VALUE_SPECIAL_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<"+keyType.getKeyType()+", "+valueType.getValueType()+", %s>" : "<"+keyType.getKeyType()+", %s>") : (valueType.isObject() ? "<"+valueType.getValueType()+", %s>" : "<%s>")).setBraceType("<>").removeBraces();
		
		addSimpleMapper(" NO_GENERIC_TYPE", keyType.isObject() ? "<?>" : "");
		addSimpleMapper(" NO_KV_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<?, ?>" : "<?>") : valueType.isObject() ? "<?>" : "");
		addSimpleMapper(" KEY_COMPAREABLE_TYPE", keyType.isObject() ? "<"+keyType.getKeyType()+" extends Comparable<T>>" : "");
		
		addSimpleMapper(" KEY_SUPER_GENERIC_TYPE", keyType.isObject() ? "<? super "+keyType.getKeyType()+">" : "");
		addSimpleMapper(" VALUE_SUPER_GENERIC_TYPE", valueType.isObject() ? "<? super "+valueType.getValueType()+">" : "");
		addSimpleMapper(" KEY_VALUE_SUPER_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<? super "+keyType.getKeyType()+", ? super "+valueType.getValueType()+">" : "<? super "+keyType.getKeyType()+">") : (valueType.isObject() ? "<? super "+valueType.getValueType()+">" : ""));
		
		addSimpleMapper(" KEY_UNKNOWN_GENERIC_TYPE", keyType.isObject() ? "<? extends "+keyType.getKeyType()+">" : "");
		addSimpleMapper(" VALUE_UNKNOWN_GENERIC_TYPE", valueType.isObject() ? "<? extends "+valueType.getValueType()+">" : "");
		addSimpleMapper(" KEY_VALUE_UNKNOWN_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<? extends "+keyType.getKeyType()+", ? extends "+valueType.getValueType()+">" : "<? extends "+keyType.getKeyType()+">") : (valueType.isObject() ? "<? extends "+valueType.getValueType()+">" : ""));

		addSimpleMapper(" KEY_ENUM_VALUE_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<"+keyType.getKeyType()+" extends Enum<"+keyType.getKeyType()+">, "+valueType.getValueType()+">" : "<"+keyType.getKeyType()+" extends Enum<"+keyType.getKeyType()+">>") : (valueType.isObject() ? "<"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" KEY_VALUE_ENUM_GENERIC_TYPE", keyType.isObject() ? (valueType.isObject() ? "<"+keyType.getKeyType()+", "+valueType.getValueType()+" extends Enum<"+valueType.getValueType()+">>" : "<"+keyType.getKeyType()+">") : (valueType.isObject() ? "<"+valueType.getValueType()+" extends Enum<"+valueType.getValueType()+">>" : ""));
		
		addInjectMapper(" KEY_SPECIAL_GENERIC_TYPE", keyType.isObject() ? "<%s>" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" VALUE_SPECIAL_GENERIC_TYPE", valueType.isObject() ? "<%s>" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" KSK_GENERIC_TYPE", keyType.isObject() ? "<%s, "+keyType.getKeyType()+">" : "<%s>").removeBraces().setBraceType("<>");
		addInjectMapper(" KKS_GENERIC_TYPE", keyType.isObject() ? "<"+keyType.getKeyType()+", %s>" : "<%s>").removeBraces().setBraceType("<>");
		addArgumentMapper(" KSS_GENERIC_TYPE", keyType.isObject() ? "<%1$s, %2$s>" : "<%2$s>").removeBraces().setBraceType("<>");		
		addInjectMapper(" SK_GENERIC_TYPE", keyType.isObject() ? "<%s, "+keyType.getKeyType()+">" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" KS_GENERIC_TYPE", keyType.isObject() ? "<"+keyType.getKeyType()+", %s>" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" VSV_GENERIC_TYPE", valueType.isObject() ? "<%s, "+valueType.getValueType()+">" : "<%s>").removeBraces().setBraceType("<>");
		addInjectMapper(" VVS_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+", %s>" : "<%s>").removeBraces().setBraceType("<>");
		addArgumentMapper(" VSS_GENERIC_TYPE", valueType.isObject() ? "<%1$s, %2$s>" : "<%2$s>").removeBraces().setBraceType("<>");	
		addInjectMapper(" SV_GENERIC_TYPE", valueType.isObject() ? "<%s, "+valueType.getValueType()+">" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" VS_GENERIC_TYPE", valueType.isObject() ? "<"+valueType.getValueType()+", %s>" : "").removeBraces().setBraceType("<>");


		addSimpleMapper(" GENERIC_KEY_BRACES", keyType.isObject() ? " <"+keyType.getKeyType()+">" : "");
		addSimpleMapper(" GENERIC_VALUE_BRACES", valueType.isObject() ? " <"+valueType.getValueType()+">" : "");
		addInjectMapper(" GENERIC_SPECIAL_KEY_BRACES", keyType.isObject() ? " <%s>" : "").removeBraces().setBraceType("<>");
		addInjectMapper(" GENERIC_SPECIAL_VALUE_BRACES", valueType.isObject() ? " <%s>" : "").removeBraces().setBraceType("<>");
		addSimpleMapper(" GENERIC_KEY_ENUM_VALUE_BRACES", keyType.isObject() ? (valueType.isObject() ? " <"+keyType.getKeyType()+" extends Enum<"+keyType.getKeyType()+">, "+valueType.getValueType()+">" : " <"+keyType.getKeyType()+" extends Enum<"+keyType.getKeyType()+">>") : (valueType.isObject() ? " <"+valueType.getValueType()+">" : ""));
		
		addInjectMapper(" GENERIC_KEY_SPECIAL_BRACES", keyType.isObject() ? " <"+keyType.getKeyType()+", %s>" : " <%s>").removeBraces().setBraceType("<>");
		addInjectMapper(" GENERIC_VALUE_SPECIAL_BRACES", valueType.isObject() ? " <"+valueType.getKeyType()+", %s>" : " <%s>").removeBraces().setBraceType("<>");
		
		addSimpleMapper(" GENERIC_KEY_VALUE_BRACES", keyType.isObject() ? (valueType.isObject() ? " <"+keyType.getKeyType()+", "+valueType.getValueType()+">" : " <"+keyType.getKeyType()+">") : (valueType.isObject() ? " <"+valueType.getValueType()+">" : ""));
		addSimpleMapper(" COMPAREABLE_KEY_BRACES", keyType.isObject() ? " <"+keyType.getKeyType()+" extends Comparable<T>>" : "");
		addSimpleMapper("KV_BRACES", keyType.isObject() || valueType.isObject() ? "<>" : "");
		addSimpleMapper("VALUE_BRACES", valueType.isObject() ? "<>" : "");
		addSimpleMapper("BRACES", keyType.isObject() ? "<>" : "");
		if(keyType.needsCustomJDKType())
		{
			addSimpleMapper("JAVA_TYPE", keyType.getCustomJDKType().getKeyType());
			addSimpleMapper("SANITY_CAST", "castTo"+keyType.getFileType());
		}
		addSimpleMapper("JAVA_CLASS", keyType.getCustomJDKType().getClassType());
		if(valueType.needsCustomJDKType())
		{
			addSimpleMapper("SANITY_CAST_VALUE", "castTo"+valueType.getFileType());
		}
		addSimpleMapper("[SPACE]", " ");
		addComment("@ArrayType", "@param <%s> the keyType of array that the operation should be applied");
		addComment("@Type", "@param <%s> the keyType of elements maintained by this Collection");
		addValueComment("@ValueArrayType", "@param <%s> the keyType of array that the operation should be applied");
		addValueComment("@ValueType", "@param <%s> the keyType of elements maintained by this Collection");
		addAnnontion("@PrimitiveOverride", "@Override");
		addSimpleMapper("@PrimitiveDoc", "");
		addAnnontion("@Primitive", "@Deprecated");
		addValueAnnontion("@ValuePrimitiveOverride", "@Override");
		addValueAnnontion("@ValuePrimitive", "@Deprecated");
	}
	
	private void createHelperVars(ClassType type, boolean value, String fix)
	{
		addArgumentMapper("EQUALS_"+fix+"_TYPE", "Objects.equals(%2$s, "+(type.isObject() ? "%1$s" : fix+"_TO_OBJ(%1$s)")+")").removeBraces();
		addInjectMapper(fix+"_EQUALS_NOT_NULL", type.getComparableValue()+" != "+(type.isPrimitiveBlocking() || type.needsCast() ? type.getEmptyValue() : "0")).removeBraces();
		addInjectMapper(fix+"_EQUALS_NULL", type.getComparableValue()+" == "+(type.isPrimitiveBlocking() || type.needsCast() ? type.getEmptyValue() : "0")).removeBraces();
		addArgumentMapper(fix+"_EQUALS_NOT", type.getEquals(true)).removeBraces();
		addArgumentMapper(fix+"_EQUALS", type.getEquals(false)).removeBraces();
		addSimpleMapper("FILE_"+fix+"_TYPE", type.getFileType());
		
		addArgumentMapper("COMPAREABLE_TO_"+fix, type.isObject() ? "((Comparable<"+type.getKeyType(value)+">)%1$s).compareTo(("+type.getKeyType(value)+")%2$s)" : type.getClassType(value)+".compare(%1$s, %2$s)").removeBraces();
		addArgumentMapper("COMPARE_TO_"+fix, type.isObject() ? "%1$s.compareTo(%2$s)" : type.getClassType(value)+".compare(%1$s, %2$s)").removeBraces();
		
		addInjectMapper(fix+"_TO_OBJ", type.isObject() ? "%s" : type.getClassType(value)+".valueOf(%s)").removeBraces();
		addInjectMapper("OBJ_TO_"+fix, type.isObject() ? "%s" : "%s."+type.getKeyType(value)+"Value()").removeBraces();
		addInjectMapper("CLASS_TO_"+fix, type.isObject() ? "("+type.getKeyType(value)+")%s" : "(("+type.getClassType(value)+")%s)."+type.getKeyType(value)+"Value()").removeBraces();
		
		addInjectMapper(fix+"_TO_HASH", type.isObject() ? "Objects.hashCode(%s)" : type.getClassType(value)+".hashCode(%s)").removeBraces();
		addInjectMapper(fix+"_TO_STRING", type.isObject() ? "Objects.toString(%s)" : type.getClassType(value)+".toString(%s)").removeBraces();
		
		addSimpleMapper("CAST_"+fix+"_ARRAY ", type.isObject() ? "("+fix+"_TYPE[])" : "");
		addSimpleMapper("EMPTY_"+fix+"_ARRAY", type.isObject() ? "("+fix+"_TYPE[])ARRAYS.EMPTY_ARRAY" : "ARRAYS.EMPTY_ARRAY");
		addInjectMapper("NEW_"+fix+"_ARRAY", type.isObject() ? "("+fix+"_TYPE[])new Object[%s]" : "new "+fix+"_TYPE[%s]").removeBraces();
		addInjectMapper("NEW_SPECIAL_"+fix+"_ARRAY", type.isObject() ? "(E[])new Object[%s]" : "new "+fix+"_TYPE[%s]").removeBraces();
		if(value) addInjectMapper("NEW_CLASS_VALUE_ARRAY", type.isObject() ? "(CLASS_VALUE_TYPE[])new Object[%s]" : "new CLASS_VALUE_TYPE[%s]").removeBraces();
		else addInjectMapper("NEW_CLASS_ARRAY", type.isObject() ? "(CLASS_TYPE[])new Object[%s]" : "new CLASS_TYPE[%s]").removeBraces();
	}
}
