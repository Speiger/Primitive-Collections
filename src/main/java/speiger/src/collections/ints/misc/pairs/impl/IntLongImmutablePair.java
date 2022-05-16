package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntLongPair;

/**
 * Mutable Pair Implementation that
 */
public class IntLongImmutablePair implements IntLongPair
{
	protected final int key;
	protected final long value;
	
	/**
	 * Default Constructor
	 */
	public IntLongImmutablePair() {
		this(0, 0L);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntLongImmutablePair(int key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntLongPair setIntKey(int key) {
		return new IntLongImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntLongPair setLongValue(long value) {
		return new IntLongImmutablePair(key, value);
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public IntLongPair set(int key, long value) {
		return new IntLongImmutablePair(key, value);
	}
	
	@Override
	public IntLongPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntLongPair) {
			IntLongPair entry = (IntLongPair)obj;
			return key == entry.getIntKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Long.toString(value);
	}
}