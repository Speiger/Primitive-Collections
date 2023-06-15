package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatShortPair;

/**
 * Mutable Pair Implementation that
 */
public class FloatShortImmutablePair implements FloatShortPair
{
	protected final float key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public FloatShortImmutablePair() {
		this(0F, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatShortImmutablePair(float key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatShortPair setFloatKey(float key) {
		return new FloatShortImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatShortPair setShortValue(short value) {
		return new FloatShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public FloatShortPair set(float key, short value) {
		return new FloatShortImmutablePair(key, value);
	}
	
	@Override
	public FloatShortPair shallowCopy() {
		return this;
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