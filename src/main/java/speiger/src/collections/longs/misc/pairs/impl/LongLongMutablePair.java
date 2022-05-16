package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongLongPair;



/**
 * Mutable Pair Implementation that
 */
public class LongLongMutablePair implements LongLongPair
{
	protected long key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public LongLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongLongMutablePair(long key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongLongPair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public LongLongPair set(long key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongLongPair shallowCopy() {
		return LongLongPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongLongPair) {
			LongLongPair entry = (LongLongPair)obj;
			return key == entry.getLongKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Long.toString(value);
	}
}