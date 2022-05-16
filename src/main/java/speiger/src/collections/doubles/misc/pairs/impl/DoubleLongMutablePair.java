package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleLongPair;



/**
 * Mutable Pair Implementation that
 */
public class DoubleLongMutablePair implements DoubleLongPair
{
	protected double key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public DoubleLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleLongMutablePair(double key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleLongPair setDoubleKey(double key) {
		this.key = key;
		return this;
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public DoubleLongPair set(double key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public DoubleLongPair shallowCopy() {
		return DoubleLongPair.mutable(key, value);
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