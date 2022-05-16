package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteBytePair;

/**
 * Mutable Pair Implementation that
 */
public class ByteByteImmutablePair implements ByteBytePair
{
	protected final byte key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public ByteByteImmutablePair() {
		this((byte)0, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteByteImmutablePair(byte key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteBytePair setByteKey(byte key) {
		return new ByteByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteBytePair setByteValue(byte value) {
		return new ByteByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public ByteBytePair set(byte key, byte value) {
		return new ByteByteImmutablePair(key, value);
	}
	
	@Override
	public ByteBytePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteBytePair) {
			ByteBytePair entry = (ByteBytePair)obj;
			return key == entry.getByteKey() && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Byte.toString(value);
	}
}