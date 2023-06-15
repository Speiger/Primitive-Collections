package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatIntPair;

/**
 * Mutable Pair Implementation that
 */
public class FloatIntImmutablePair implements FloatIntPair
{
	protected final float key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public FloatIntImmutablePair() {
		this(0F, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatIntImmutablePair(float key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatIntPair setFloatKey(float key) {
		return new FloatIntImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatIntPair setIntValue(int value) {
		return new FloatIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public FloatIntPair set(float key, int value) {
		return new FloatIntImmutablePair(key, value);
	}
	
	@Override
	public FloatIntPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatIntPair) {
			FloatIntPair entry = (FloatIntPair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Integer.toString(value);
	}
}