package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteIntPair;



/**
 * Mutable Pair Implementation that
 */
public class ByteIntMutablePair implements ByteIntPair
{
	protected byte key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public ByteIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteIntMutablePair(byte key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteIntPair setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public ByteIntPair set(byte key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteIntPair shallowCopy() {
		return ByteIntPair.mutable(key, value);
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