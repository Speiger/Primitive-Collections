package speiger.src.collections.bytes.misc.pairs.impl;


import speiger.src.collections.bytes.misc.pairs.ByteLongPair;

/**
 * Mutable Pair Implementation that
 */
public class ByteLongImmutablePair implements ByteLongPair
{
	protected final byte key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public ByteLongImmutablePair() {
		this((byte)0, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ByteLongImmutablePair(byte key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ByteLongPair setByteKey(byte key) {
		return new ByteLongImmutablePair(key, value);
	}
	
	@Override
	public byte getByteKey() {
		return key;
	}
	
	@Override
	public ByteLongPair setLongValue(long value) {
		return new ByteLongImmutablePair(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public ByteLongPair set(byte key, long value) {
		return new ByteLongImmutablePair(key, value);
	}
	
	@Override
	public ByteLongPair shallowCopy() {
		return this;
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