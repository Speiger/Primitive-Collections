package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleDoublePair;

/**
 * Mutable Pair Implementation that
 */
public class DoubleDoubleImmutablePair implements DoubleDoublePair
{
	protected final double key;
	protected final double value;
	
	/**
	 * Default Constructor
	 */
	public DoubleDoubleImmutablePair() {
		this(0D, 0D);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleDoubleImmutablePair(double key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleDoublePair setDoubleKey(double key) {
		return new DoubleDoubleImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleDoublePair setDoubleValue(double value) {
		return new DoubleDoubleImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public DoubleDoublePair set(double key, double value) {
		return new DoubleDoubleImmutablePair(key, value);
	}
	
	@Override
	public DoubleDoublePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleDoublePair) {
			DoubleDoublePair entry = (DoubleDoublePair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Double.toString(value);
	}
}