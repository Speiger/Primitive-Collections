package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortBytePair;

/**
 * Mutable Pair Implementation that
 */
public class ShortByteImmutablePair implements ShortBytePair
{
	protected final short key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public ShortByteImmutablePair() {
		this((short)0, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortByteImmutablePair(short key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortBytePair setShortKey(short key) {
		return new ShortByteImmutablePair(key, value);
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortBytePair setByteValue(byte value) {
		return new ShortByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public ShortBytePair set(short key, byte value) {
		return new ShortByteImmutablePair(key, value);
	}
	
	@Override
	public ShortBytePair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortBytePair) {
			ShortBytePair entry = (ShortBytePair)obj;
			return key == entry.getShortKey() && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Byte.toString(value);
	}
}