package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteShortPair;

/**
 * Mutable Pair Implementation that
 */
public class ByteShortImmutablePair implements ByteShortPair
{
	protected final byte key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public ByteShortImmutablePair() {
		this((byte)0, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteShortImmutablePair(byte key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteShortPair setByteKey(byte key) {
		return new ByteShortImmutablePair(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteShortPair setShortValue(short value) {
		return new ByteShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public ByteShortPair set(byte key, short value) {
		return new ByteShortImmutablePair(key, value);
	}
	
	@Override
	public ByteShortPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteShortPair) {
			ByteShortPair entry = (ByteShortPair)obj;
			return key == entry.getByteKey() && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Short.toString(value);
	}
}