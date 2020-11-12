package speiger.src.builder.example;

public enum ClassType
{
	BOOLEAN("boolean", "Boolean", "Boolean", "booleans", "BOOLEAN"),
	BYTE("byte", "Byte", "Byte", "bytes", "BYTES"),
	SHORT("short", "Short", "Short", "shorts", "SHORT"),
	CHAR("char", "Character", "Char", "chars", "CHAR"),
	INT("int", "Integer", "Int", "ints", "INT"),
	LONG("long", "Long", "Long", "longs", "LONG"),
	FLOAT("float", "Float", "Float", "floats", "FLOAT"),
	DOUBLE("double", "Double", "Double", "doubles", "DOUBLE"),
	OBJECT("T", "T", "Object", "objects", "OBJECT");
	
	String keyType;
	String classType;
	String fileType;
	String pathType;
	String capType;
	
	private ClassType(String keyType, String classType, String fileType, String pathType, String capType)
	{
		this.keyType = keyType;
		this.classType = classType;
		this.fileType = fileType;
		this.pathType = pathType;
		this.capType = capType;
	}
	
	public String getKeyType()
	{
		return keyType;
	}
	
	public String getClassType()
	{
		return classType;
	}
	
	public String getNonClassType()
	{
		return this == OBJECT ? "" : classType;
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
	
	public boolean isPrimitiveBlocking()
	{
		return this == BOOLEAN || this == OBJECT;
	}
	
	public boolean needsCustomJDKType()
	{
		return this == BYTE || this == SHORT || this == CHAR || this == FLOAT;
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
