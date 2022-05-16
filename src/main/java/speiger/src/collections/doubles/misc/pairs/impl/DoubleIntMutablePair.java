package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleIntPair;



/**
 * Mutable Pair Implementation that
 */
public class DoubleIntMutablePair implements DoubleIntPair
{
	protected double key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public DoubleIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleIntMutablePair(double key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleIntPair setDoubleKey(double key) {
		this.key = key;
		return this;
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public DoubleIntPair set(double key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public DoubleIntPair shallowCopy() {
		return DoubleIntPair.mutable(key, value);
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