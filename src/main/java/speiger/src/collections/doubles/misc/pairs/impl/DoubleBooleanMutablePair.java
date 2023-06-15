package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleBooleanPair;



/**
 * Mutable Pair Implementation that
 */
public class DoubleBooleanMutablePair implements DoubleBooleanPair
{
	protected double key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public DoubleBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleBooleanMutablePair(double key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleBooleanPair setDoubleKey(double key) {
		this.key = key;
		return this;
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleBooleanPair setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public DoubleBooleanPair set(double key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public DoubleBooleanPair shallowCopy() {
		return DoubleBooleanPair.mutable(key, value);
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