package speiger.src.builder.example;

public enum ClassType
{
	BOOLEAN("boolean", "Boolean", "Boolean", "booleans", "BOOLEAN", "false"),
	BYTE("byte", "Byte", "Byte", "bytes", "BYTES", "(byte)0"),
	SHORT("short", "Short", "Short", "shorts", "SHORT", "(short)0"),
	CHAR("char", "Character", "Char", "chars", "CHAR", "(char)0"),
	INT("int", "Integer", "Int", "ints", "INT", "0"),
	LONG("long", "Long", "Long", "longs", "LONG", "0L"),
	FLOAT("float", "Float", "Float", "floats", "FLOAT", "0F"),
	DOUBLE("double", "Double", "Double", "doubles", "DOUBLE", "0D"),
	OBJECT("T", "T", "Object", "objects", "OBJECT", "null");
	
	String keyType;
	String classType;
	String fileType;
	String pathType;
	String capType;
	String emptyValue;
	
	private ClassType(String keyType, String classType, String fileType, String pathType, String capType, String emptyValue)
	{
		this.keyType = keyType;
		this.classType = classType;
		this.fileType = fileType;
		this.pathType = pathType;
		this.capType = capType;
		this.emptyValue = emptyValue;
	}
	
	public String getKeyType()
	{
		return keyType;
	}
	
	public String getClassType()
	{
		return classType;
	}
	
	public String getNonFileType()
	{
		return this == OBJECT ? "" : fileType;
	}
	
	public String getFileType()
	{
		return fileType;
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
	
	public boolean isPrimitiveBlocking()
	{
		return this == BOOLEAN || this == OBJECT;
	}
	
	public boolean needsCustomJDKType()
	{
		return this == BYTE || this == SHORT || this == CHAR || this == FLOAT;
	}
	
	public String getEquals()
	{
		switch(this)
		{
			case DOUBLE: return "Double.doubleToLongBits(%1$s) == Double.doubleToLongBits(%2$s)";
			case FLOAT: return "Float.floatToIntBits(%1$s) == Float.floatToIntBits(%2$s)";
			case OBJECT: return "Objects.equals(%1$s, %2$s)";
			default: return "%1$s == %2$s";
		}
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
