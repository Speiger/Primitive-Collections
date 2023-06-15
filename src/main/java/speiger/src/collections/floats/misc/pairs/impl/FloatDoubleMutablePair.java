package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatDoublePair;



/**
 * Mutable Pair Implementation that
 */
public class FloatDoubleMutablePair implements FloatDoublePair
{
	protected float key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public FloatDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatDoubleMutablePair(float key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatDoublePair setFloatKey(float key) {
		this.key = key;
		return this;
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatDoublePair setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public FloatDoublePair set(float key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public FloatDoublePair shallowCopy() {
		return FloatDoublePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatDoublePair) {
			FloatDoublePair entry = (FloatDoublePair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Double.toString(value);
	}
}