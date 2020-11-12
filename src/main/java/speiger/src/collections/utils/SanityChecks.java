package speiger.src.collections.utils;

public class SanityChecks
{
	public static byte castToByte(int value)
	{
		if(value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) throw new IllegalStateException("Value ["+value+"] out of Byte[-128,127] range");
		return (byte)value;
	}
	
	public static short castToShort(int value)
	{
		if(value > Short.MAX_VALUE || value < Short.MIN_VALUE) throw new IllegalStateException("Value ["+value+"] out of Short[-32768,32767] range");
		return (short)value;
	}
	
	public static char castToChar(int value)
	{
		if(value > Character.MAX_VALUE || value < Character.MIN_VALUE) throw new IllegalStateException("Value ["+value+"] out of Character[0,65535] range");
		return (char)value;
	}
	
	public static float castToFloat(double value)
	{
		if(Double.isNaN(value)) return Float.NaN;
		if(Double.isInfinite(value)) return value > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
		if(value < -Float.MAX_VALUE || value > Float.MAX_VALUE) throw new IllegalStateException("Value ["+value+"] out of Float range");
		return (float)value;
	}
}
