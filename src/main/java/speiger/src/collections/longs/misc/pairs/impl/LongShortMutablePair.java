package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongShortPair;



/**
 * Mutable Pair Implementation that
 */
public class LongShortMutablePair implements LongShortPair
{
	protected long key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public LongShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongShortMutablePair(long key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongShortPair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongShortPair setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public LongShortPair set(long key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongShortPair shallowCopy() {
		return LongShortPair.mutable(key, value);
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