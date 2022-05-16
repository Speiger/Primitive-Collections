package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanDoublePair;

/**
 * Mutable Pair Implementation that
 */
public class BooleanDoubleImmutablePair implements BooleanDoublePair
{
	protected final boolean key;
	protected final double value;
	
	/**
	 * Default Constructor
	 */
	public BooleanDoubleImmutablePair() {
		this(false, 0D);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanDoubleImmutablePair(boolean key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanDoublePair setBooleanKey(boolean key) {
		return new BooleanDoubleImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanDoublePair setDoubleValue(double value) {
		return new BooleanDoubleImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public BooleanDoublePair set(boolean key, double value) {
		return new BooleanDoubleImmutablePair(key, value);
	}
	
	@Override
	public BooleanDoublePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanDoublePair) {
			BooleanDoublePair entry = (BooleanDoublePair)obj;
			return key == entry.getBooleanKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Double.toString(value);
	}
}