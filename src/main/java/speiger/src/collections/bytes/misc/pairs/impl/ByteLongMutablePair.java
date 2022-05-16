package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteLongPair;



/**
 * Mutable Pair Implementation that
 */
public class ByteLongMutablePair implements ByteLongPair
{
	protected byte key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public ByteLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteLongMutablePair(byte key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteLongPair setByteKey(byte key) {
		this.key = key;
		return this;
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public ByteLongPair set(byte key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ByteLongPair shallowCopy() {
		return ByteLongPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteLongPair) {
			ByteLongPair entry = (ByteLongPair)obj;
			return key == entry.getByteKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Byte.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Byte.toString(key) + "->" + Long.toString(value);
	}
}