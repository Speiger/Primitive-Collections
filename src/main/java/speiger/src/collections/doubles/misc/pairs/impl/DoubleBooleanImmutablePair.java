package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleBooleanPair;

/**
 * Mutable Pair Implementation that
 */
public class DoubleBooleanImmutablePair implements DoubleBooleanPair
{
	protected final double key;
	protected final boolean value;
	
	/**
	 * Default Constructor
	 */
	public DoubleBooleanImmutablePair() {
		this(0D, false);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleBooleanImmutablePair(double key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleBooleanPair setDoubleKey(double key) {
		return new DoubleBooleanImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleBooleanPair setBooleanValue(boolean value) {
		return new DoubleBooleanImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public DoubleBooleanPair set(double key, boolean value) {
		return new DoubleBooleanImmutablePair(key, value);
	}
	
	@Override
	public DoubleBooleanPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleBooleanPair) {
			DoubleBooleanPair entry = (DoubleBooleanPair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Boolean.toString(value);
	}
}