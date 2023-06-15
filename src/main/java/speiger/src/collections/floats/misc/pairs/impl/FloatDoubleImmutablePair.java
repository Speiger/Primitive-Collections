package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatDoublePair;

/**
 * Mutable Pair Implementation that
 */
public class FloatDoubleImmutablePair implements FloatDoublePair
{
	protected final float key;
	protected final double value;
	
	/**
	 * Default Constructor
	 */
	public FloatDoubleImmutablePair() {
		this(0F, 0D);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatDoubleImmutablePair(float key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatDoublePair setFloatKey(float key) {
		return new FloatDoubleImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatDoublePair setDoubleValue(double value) {
		return new FloatDoubleImmutablePair(key, value);
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public FloatDoublePair set(float key, double value) {
		return new FloatDoubleImmutablePair(key, value);
	}
	
	@Override
	public FloatDoublePair shallowCopy() {
		return this;
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