package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteShortPair;



/**
 * Mutable Pair Implementation that
 */
public class ByteShortMutablePair implements ByteShortPair
{
	protected byte key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public ByteShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteShortMutablePair(byte key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteShortPair setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteShortPair setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public ByteShortPair set(byte key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteShortPair shallowCopy() {
		return ByteShortPair.mutable(key, value);
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