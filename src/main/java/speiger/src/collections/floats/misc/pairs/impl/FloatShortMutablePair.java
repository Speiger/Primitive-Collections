package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatShortPair;



/**
 * Mutable Pair Implementation that
 */
public class FloatShortMutablePair implements FloatShortPair
{
	protected float key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public FloatShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatShortMutablePair(float key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatShortPair setFloatKey(float key) {
		this.key = key;
		return this;
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatShortPair setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public FloatShortPair set(float key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public FloatShortPair shallowCopy() {
		return FloatShortPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatShortPair) {
			FloatShortPair entry = (FloatShortPair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Short.toString(value);
	}
}