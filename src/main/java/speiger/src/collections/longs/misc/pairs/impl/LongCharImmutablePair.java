package speiger.src.collections.longs.misc.pairs.impl;


import speiger.src.collections.longs.misc.pairs.LongCharPair;

/**
 * Mutable Pair Implementation that
 */
public class LongCharImmutablePair implements LongCharPair
{
	protected final long key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public LongCharImmutablePair() {
		this(0L, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public LongCharImmutablePair(long key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public LongCharPair setLongKey(long key) {
		return new LongCharImmutablePair(key, value);
	}
	
	@Override
	public long getLongKey() {
		return key;
	}
	
	@Override
	public LongCharPair setCharValue(char value) {
		return new LongCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public LongCharPair set(long key, char value) {
		return new LongCharImmutablePair(key, value);
	}
	
	@Override
	public LongCharPair shallowCopy() {
		return this;
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