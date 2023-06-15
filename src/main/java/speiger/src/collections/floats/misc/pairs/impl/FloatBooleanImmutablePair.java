package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatBooleanPair;

/**
 * Mutable Pair Implementation that
 */
public class FloatBooleanImmutablePair implements FloatBooleanPair
{
	protected final float key;
	protected final boolean value;
	
	/**
	 * Default Constructor
	 */
	public FloatBooleanImmutablePair() {
		this(0F, false);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatBooleanImmutablePair(float key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatBooleanPair setFloatKey(float key) {
		return new FloatBooleanImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatBooleanPair setBooleanValue(boolean value) {
		return new FloatBooleanImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public FloatBooleanPair set(float key, boolean value) {
		return new FloatBooleanImmutablePair(key, value);
	}
	
	@Override
	public FloatBooleanPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatBooleanPair) {
			FloatBooleanPair entry = (FloatBooleanPair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Boolean.toString(value);
	}
}