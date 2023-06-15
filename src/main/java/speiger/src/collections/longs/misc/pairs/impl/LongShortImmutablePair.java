package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongShortPair;

/**
 * Mutable Pair Implementation that
 */
public class LongShortImmutablePair implements LongShortPair
{
	protected final long key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public LongShortImmutablePair() {
		this(0L, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongShortImmutablePair(long key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongShortPair setLongKey(long key) {
		return new LongShortImmutablePair(key, value);
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongShortPair setShortValue(short value) {
		return new LongShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public LongShortPair set(long key, short value) {
		return new LongShortImmutablePair(key, value);
	}
	
	@Override
	public LongShortPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongShortPair) {
			LongShortPair entry = (LongShortPair)obj;
			return key == entry.getLongKey() && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Short.toString(value);
	}
}