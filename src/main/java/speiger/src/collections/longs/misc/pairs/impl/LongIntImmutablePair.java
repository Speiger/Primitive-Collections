package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongIntPair;

/**
 * Mutable Pair Implementation that
 */
public class LongIntImmutablePair implements LongIntPair
{
	protected final long key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public LongIntImmutablePair() {
		this(0L, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongIntImmutablePair(long key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongIntPair setLongKey(long key) {
		return new LongIntImmutablePair(key, value);
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongIntPair setIntValue(int value) {
		return new LongIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public LongIntPair set(long key, int value) {
		return new LongIntImmutablePair(key, value);
	}
	
	@Override
	public LongIntPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongIntPair) {
			LongIntPair entry = (LongIntPair)obj;
			return key == entry.getLongKey() && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Integer.toString(value);
	}
}