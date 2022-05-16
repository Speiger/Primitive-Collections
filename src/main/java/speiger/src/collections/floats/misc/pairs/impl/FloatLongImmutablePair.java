package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatLongPair;

/**
 * Mutable Pair Implementation that
 */
public class FloatLongImmutablePair implements FloatLongPair
{
	protected final float key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public FloatLongImmutablePair() {
		this(0F, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatLongImmutablePair(float key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatLongPair setFloatKey(float key) {
		return new FloatLongImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatLongPair setLongValue(long value) {
		return new FloatLongImmutablePair(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public FloatLongPair set(float key, long value) {
		return new FloatLongImmutablePair(key, value);
	}
	
	@Override
	public FloatLongPair shallowCopy() {
		return this;
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