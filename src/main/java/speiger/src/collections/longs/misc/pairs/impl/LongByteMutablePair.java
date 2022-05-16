package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongBytePair;



/**
 * Mutable Pair Implementation that
 */
public class LongByteMutablePair implements LongBytePair
{
	protected long key;
	protected byte value;
	
	/**
	 * Default Constructor
	 */
	public LongByteMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongByteMutablePair(long key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongBytePair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongBytePair setByteValue(byte value) {
		this.value = value;
		return this;
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public LongBytePair set(long key, byte value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongBytePair shallowCopy() {
		return LongBytePair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongBytePair) {
			LongBytePair entry = (LongBytePair)obj;
			return key == entry.getLongKey() && value == entry.getByteValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Byte.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Byte.toString(value);
	}
}