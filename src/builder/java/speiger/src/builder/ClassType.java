package speiger.src.builder;

@SuppressWarnings("javadoc")
public enum ClassType
{
	BOOLEAN("boolean", "Boolean", "Boolean", "booleans", "BOOLEAN", "false", "false"),
	BYTE("byte", "Byte", "Byte", "bytes", "BYTE", "(byte)0", "(byte)-1"),
	SHORT("short", "Short", "Short", "shorts", "SHORT", "(short)0", "(short)-1"),
	CHAR("char", "Character", "Char", "chars", "CHAR", "(char)0", "(char)-1"),
	INT("int", "Integer", "Int", "ints", "INT", "0", "-1"),
	LONG("long", "Long", "Long", "longs", "LONG", "0L", "-1L"),
	FLOAT("float", "Float", "Float", "floats", "FLOAT", "0F", "-1F"),
	DOUBLE("double", "Double", "Double", "doubles", "DOUBLE", "0D", "-1D"),
	OBJECT("T", "T", "Object", "objects", "OBJECT", "null", "null");
	
	String keyType;
	String classType;
	String fileType;
	String pathType;
	String capType;
	String emptyValue;
	String invalidValue;
	
	private ClassType(String keyType, String classType, String fileType, String pathType, String capType, String emptyValue, String invalidValue)
	{
		this.keyType = keyType;
		this.classType = classType;
		this.fileType = fileType;
		this.pathType = pathType;
		this.capType = capType;
		this.emptyValue = emptyValue;
		this.invalidValue = invalidValue;
	}
	
	public String getKeyType()
	{
		return keyType;
	}
	
	public String getKeyType(boolean value)
	{
		return value && this == OBJECT ? "V" : keyType;
	}
	
	public String getValueType()
	{
		return this == OBJECT ? "V" : keyType;
	}
	
	public String getClassType()
	{
		return classType;
	}
	
	public String getClassPath()
	{
		return this == OBJECT ? "Object" : classType;
	}
	
	public String getClassType(boolean value) 
	{
		return value && this == OBJECT ? "V" : classType;
	}
	
	public String getClassValueType()
	{
		return this == OBJECT ? "V" : classType;
	}
	
	public String getNonFileType()
	{
		return this == OBJECT ? "" : fileType;
	}
	
	public String getFileType()
	{
		return fileType;
	}
	
	public String getJavaFileType()
	{
		return this == OBJECT ? "Obj" : fileType;
	}
	
	public String getPathType()
	{
		return pathType;
	}
	
	public String getCapType()
	{
		return capType;
	}
	
	public String getEmptyValue()
	{
		return emptyValue;
	}
	
	public String getInvalidValue()
	{
		return invalidValue;
	}
	
	public boolean isObject()
	{
		return this == OBJECT;
	}
	
	public boolean isPrimitiveBlocking()
	{
		return this == BOOLEAN || this == OBJECT;
	}
	
	public boolean needsCustomJDKType()
	{
		return this == BYTE || this == SHORT || this == CHAR || this == FLOAT;
	}
	
	public boolean needsCast()
	{
		return this == BYTE || this == SHORT || this == CHAR;
	}
	
	public String getComparableValue()
	{
		switch(this)
		{
			case DOUBLE: return "Double.doubleToLongBits(%1$s)";
			case FLOAT: return "Float.floatToIntBits(%1$s)";
			case OBJECT: return "%1$s";
			default: return "%1$s";
		}
	}
	
	public String getApply(ClassType other) {
		if(other == BOOLEAN) return "test";
		if(other == ClassType.OBJECT) return "apply";
		return "applyAs"+other.getFileType();
	}
	
	public String getEquals(boolean not)
	{
		switch(this)
		{
			case DOUBLE: return "Double.doubleToLongBits(%1$s) "+(not ? "!=" : "==")+" Double.doubleToLongBits(%2$s)";
			case FLOAT: return "Float.floatToIntBits(%1$s) "+(not ? "!=" : "==")+" Float.floatToIntBits(%2$s)";
			case OBJECT: return (not ? "!" : "")+"Objects.equals(%1$s, %2$s)";
			default: return "%1$s "+(not ? "!=" : "==")+" %2$s";
		}
	}
	
	public boolean hasFunction(ClassType other)
	{
		if(this == other && this != BOOLEAN && !needsCustomJDKType() && !other.needsCustomJDKType()) return true;
		if(this == BOOLEAN) return false;
		if(other == BOOLEAN && !needsCustomJDKType()) return true;
		if(!needsCustomJDKType() && !other.needsCustomJDKType()) return true;
		return false;
	}
	
	public String getFunctionClass(ClassType other)
	{
		if(!hasFunction(other)) return "";
		if(this == other && this != BOOLEAN) return this == OBJECT ? "java.util.function.Function" : "java.util.function."+getJavaFileType()+"UnaryOperator";
		if(other == BOOLEAN) return this == OBJECT ? "java.util.function.Predicate" : "java.util.function."+getJavaFileType()+"Predicate";
		if(!needsCustomJDKType()) return other == OBJECT ? "java.util.function."+getJavaFileType()+"Function" : (this == OBJECT ? "java.util.function.To"+other.getJavaFileType()+"Function" : "java.util.function."+getJavaFileType()+"To"+other.getJavaFileType()+"Function");
		if(!other.needsCustomJDKType()) return this == OBJECT ? "java.util.function.To"+other.getJavaFileType()+"Function" : "java.util.function."+getJavaFileType()+"To"+other.getJavaFileType()+"Function";
		return "";
	}
	
	public ClassType getCustomJDKType()
	{
		switch(this)
		{
			case BYTE: return INT;
			case CHAR: return INT;
			case FLOAT: return DOUBLE;
			case SHORT: return INT;
			default: return this;
		}
	}
}
