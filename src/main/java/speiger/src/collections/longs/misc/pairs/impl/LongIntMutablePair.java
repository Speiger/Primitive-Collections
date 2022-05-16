package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongIntPair;



/**
 * Mutable Pair Implementation that
 */
public class LongIntMutablePair implements LongIntPair
{
	protected long key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public LongIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongIntMutablePair(long key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongIntPair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public LongIntPair set(long key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongIntPair shallowCopy() {
		return LongIntPair.mutable(key, value);
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