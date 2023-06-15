package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleIntPair;

/**
 * Mutable Pair Implementation that
 */
public class DoubleIntImmutablePair implements DoubleIntPair
{
	protected final double key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public DoubleIntImmutablePair() {
		this(0D, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleIntImmutablePair(double key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleIntPair setDoubleKey(double key) {
		return new DoubleIntImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleIntPair setIntValue(int value) {
		return new DoubleIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public DoubleIntPair set(double key, int value) {
		return new DoubleIntImmutablePair(key, value);
	}
	
	@Override
	public DoubleIntPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleIntPair) {
			DoubleIntPair entry = (DoubleIntPair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Integer.toString(value);
	}
}