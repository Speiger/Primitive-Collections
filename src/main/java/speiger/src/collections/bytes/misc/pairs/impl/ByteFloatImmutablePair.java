package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteFloatPair;

/**
 * Mutable Pair Implementation that
 */
public class ByteFloatImmutablePair implements ByteFloatPair
{
	protected final byte key;
	protected final float value;
	
	/**
	 * Default Constructor
	 */
	public ByteFloatImmutablePair() {
		this((byte)0, 0F);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteFloatImmutablePair(byte key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteFloatPair setByteKey(byte key) {
		return new ByteFloatImmutablePair(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteFloatPair setFloatValue(float value) {
		return new ByteFloatImmutablePair(key, value);
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public ByteFloatPair set(byte key, float value) {
		return new ByteFloatImmutablePair(key, value);
	}
	
	@Override
	public ByteFloatPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteFloatPair) {
			ByteFloatPair entry = (ByteFloatPair)obj;
			return key == entry.getByteKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Float.toString(value);
	}
}