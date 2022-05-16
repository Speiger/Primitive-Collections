package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatIntPair;



/**
 * Mutable Pair Implementation that
 */
public class FloatIntMutablePair implements FloatIntPair
{
	protected float key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public FloatIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatIntMutablePair(float key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatIntPair setFloatKey(float key) {
		this.key = key;
		return this;
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public FloatIntPair set(float key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public FloatIntPair shallowCopy() {
		return FloatIntPair.mutable(key, value);
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