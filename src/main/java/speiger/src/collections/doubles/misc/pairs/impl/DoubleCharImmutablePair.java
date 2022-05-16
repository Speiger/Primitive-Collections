package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleCharPair;

/**
 * Mutable Pair Implementation that
 */
public class DoubleCharImmutablePair implements DoubleCharPair
{
	protected final double key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public DoubleCharImmutablePair() {
		this(0D, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleCharImmutablePair(double key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleCharPair setDoubleKey(double key) {
		return new DoubleCharImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleCharPair setCharValue(char value) {
		return new DoubleCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public DoubleCharPair set(double key, char value) {
		return new DoubleCharImmutablePair(key, value);
	}
	
	@Override
	public DoubleCharPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleCharPair) {
			DoubleCharPair entry = (DoubleCharPair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Character.toString(value);
	}
}