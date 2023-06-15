package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntDoublePair;

/**
 * Mutable Pair Implementation that
 */
public class IntDoubleImmutablePair implements IntDoublePair
{
	protected final int key;
	protected final double value;
	
	/**
	 * Default Constructor
	 */
	public IntDoubleImmutablePair() {
		this(0, 0D);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntDoubleImmutablePair(int key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntDoublePair setIntKey(int key) {
		return new IntDoubleImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntDoublePair setDoubleValue(double value) {
		return new IntDoubleImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public IntDoublePair set(int key, double value) {
		return new IntDoubleImmutablePair(key, value);
	}
	
	@Override
	public IntDoublePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntDoublePair) {
			IntDoublePair entry = (IntDoublePair)obj;
			return key == entry.getIntKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Double.toString(value);
	}
}