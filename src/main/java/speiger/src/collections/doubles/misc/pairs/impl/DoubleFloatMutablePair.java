package speiger.src.collections.doubles.misc.pairs.impl;


import speiger.src.collections.doubles.misc.pairs.DoubleFloatPair;



/**
 * Mutable Pair Implementation that
 */
public class DoubleFloatMutablePair implements DoubleFloatPair
{
	protected double key;
	protected float value;
	
	/**
	 * Default Constructor
	 */
	public DoubleFloatMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public DoubleFloatMutablePair(double key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public DoubleFloatPair setDoubleKey(double key) {
		this.key = key;
		return this;
	}
	
	@Override
	public double getDoubleKey() {
		return key;
	}
	
	@Override
	public DoubleFloatPair setFloatValue(float value) {
		this.value = value;
		return this;
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public DoubleFloatPair set(double key, float value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public DoubleFloatPair shallowCopy() {
		return DoubleFloatPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DoubleFloatPair) {
			DoubleFloatPair entry = (DoubleFloatPair)obj;
			return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(key) + "->" + Float.toString(value);
	}
}