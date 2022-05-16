package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleLongPair;

/**
 * Mutable Pair Implementation that
 */
public class DoubleLongImmutablePair implements DoubleLongPair
{
	protected final double key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public DoubleLongImmutablePair() {
		this(0D, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleLongImmutablePair(double key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleLongPair setDoubleKey(double key) {
		return new DoubleLongImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleLongPair setLongValue(long value) {
		return new DoubleLongImmutablePair(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public DoubleLongPair set(double key, long value) {
		return new DoubleLongImmutablePair(key, value);
	}
	
	@Override
	public DoubleLongPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleLongPair) {
			DoubleLongPair entry = (DoubleLongPair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Long.toString(value);
	}
}