package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatLongPair;



/**
 * Mutable Pair Implementation that
 */
public class FloatLongMutablePair implements FloatLongPair
{
	protected float key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public FloatLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatLongMutablePair(float key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatLongPair setFloatKey(float key) {
		this.key = key;
		return this;
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public FloatLongPair set(float key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public FloatLongPair shallowCopy() {
		return FloatLongPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatLongPair) {
			FloatLongPair entry = (FloatLongPair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Long.toString(value);
	}
}