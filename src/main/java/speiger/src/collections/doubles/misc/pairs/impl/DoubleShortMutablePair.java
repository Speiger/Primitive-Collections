package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleShortPair;



/**
 * Mutable Pair Implementation that
 */
public class DoubleShortMutablePair implements DoubleShortPair
{
	protected double key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public DoubleShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleShortMutablePair(double key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleShortPair setDoubleKey(double key) {
		this.key = key;
		return this;
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleShortPair setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public DoubleShortPair set(double key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public DoubleShortPair shallowCopy() {
		return DoubleShortPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleShortPair) {
			DoubleShortPair entry = (DoubleShortPair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Short.toString(value);
	}
}