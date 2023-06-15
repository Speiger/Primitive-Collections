package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongCharPair;



/**
 * Mutable Pair Implementation that
 */
public class LongCharMutablePair implements LongCharPair
{
	protected long key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public LongCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongCharMutablePair(long key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongCharPair setLongKey(long key) {
		this.key = key;
		return this;
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongCharPair setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public LongCharPair set(long key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public LongCharPair shallowCopy() {
		return LongCharPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LongCharPair) {
			LongCharPair entry = (LongCharPair)obj;
			return key == entry.getLongKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Long.toString(key) + "->" + Character.toString(value);
	}
}