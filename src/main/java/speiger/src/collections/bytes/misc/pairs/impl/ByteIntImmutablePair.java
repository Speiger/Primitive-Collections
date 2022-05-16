package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteIntPair;

/**
 * Mutable Pair Implementation that
 */
public class ByteIntImmutablePair implements ByteIntPair
{
	protected final byte key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public ByteIntImmutablePair() {
		this((byte)0, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteIntImmutablePair(byte key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteIntPair setByteKey(byte key) {
		return new ByteIntImmutablePair(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteIntPair setIntValue(int value) {
		return new ByteIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public ByteIntPair set(byte key, int value) {
		return new ByteIntImmutablePair(key, value);
	}
	
	@Override
	public ByteIntPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteIntPair) {
			ByteIntPair entry = (ByteIntPair)obj;
			return key == entry.getByteKey() && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Integer.toString(value);
	}
}