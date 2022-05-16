package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortBytePair;



/**
 * Mutable Pair Implementation that
 */
public class ShortByteMutablePair implements ShortBytePair
{
	protected short key;
	protected byte value;
	
	/**
	 * Default Constructor
	 */
	public ShortByteMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortByteMutablePair(short key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortBytePair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortBytePair setByteValue(byte value) {
		this.value = value;
		return this;
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public ShortBytePair set(short key, byte value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortBytePair shallowCopy() {
		return ShortBytePair.mutable(key, value);
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