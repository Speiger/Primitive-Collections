package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteBytePair;



/**
 * Mutable Pair Implementation that
 */
public class ByteByteMutablePair implements ByteBytePair
{
	protected byte key;
	protected byte value;
	
	/**
	 * Default Constructor
	 */
	public ByteByteMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteByteMutablePair(byte key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteBytePair setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteBytePair setByteValue(byte value) {
		this.value = value;
		return this;
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public ByteBytePair set(byte key, byte value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteBytePair shallowCopy() {
		return ByteBytePair.mutable(key, value);
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