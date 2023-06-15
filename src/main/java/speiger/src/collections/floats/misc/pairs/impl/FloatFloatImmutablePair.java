package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatFloatPair;

/**
 * Mutable Pair Implementation that
 */
public class FloatFloatImmutablePair implements FloatFloatPair
{
	protected final float key;
	protected final float value;
	
	/**
	 * Default Constructor
	 */
	public FloatFloatImmutablePair() {
		this(0F, 0F);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatFloatImmutablePair(float key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatFloatPair setFloatKey(float key) {
		return new FloatFloatImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatFloatPair setFloatValue(float value) {
		return new FloatFloatImmutablePair(key, value);
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public FloatFloatPair set(float key, float value) {
		return new FloatFloatImmutablePair(key, value);
	}
	
	@Override
	public FloatFloatPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatFloatPair) {
			FloatFloatPair entry = (FloatFloatPair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Float.toString(value);
	}
}