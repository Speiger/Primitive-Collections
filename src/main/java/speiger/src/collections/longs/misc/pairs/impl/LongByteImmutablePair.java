package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongBytePair;

/**
 * Mutable Pair Implementation that
 */
public class LongByteImmutablePair implements LongBytePair
{
	protected final long key;
	protected final byte value;
	
	/**
	 * Default Constructor
	 */
	public LongByteImmutablePair() {
		this(0L, (byte)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongByteImmutablePair(long key, byte value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongBytePair setLongKey(long key) {
		return new LongByteImmutablePair(key, value);
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongBytePair setByteValue(byte value) {
		return new LongByteImmutablePair(key, value);
	}
	
	@Override
	public byte getByteValue() {
		return value;
	}
	
	@Override
	public LongBytePair set(long key, byte value) {
		return new LongByteImmutablePair(key, value);
	}
	
	@Override
	public LongBytePair shallowCopy() {
		return this;
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