package speiger.src.collections.floats.misc.pairs.impl;


import speiger.src.collections.floats.misc.pairs.FloatBytePair;

/**
 * Mutable Pair Implementation that
 */
public class FloatByteImmutablePair implements FloatBytePair
{
	protected final float key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public FloatByteImmutablePair() {
		this(0F, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public FloatByteImmutablePair(float key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public FloatBytePair setFloatKey(float key) {
		return new FloatByteImmutablePair(key, value);
	}
	
	@Override
	public float getFloatKey() {
		return key;
	}
	
	@Override
	public FloatBytePair setByteValue(byte value) {
		return new FloatByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public FloatBytePair set(float key, byte value) {
		return new FloatByteImmutablePair(key, value);
	}
	
	@Override
	public FloatBytePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FloatBytePair) {
			FloatBytePair entry = (FloatBytePair)obj;
			return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Float.toString(key) + "->" + Byte.toString(value);
	}
}