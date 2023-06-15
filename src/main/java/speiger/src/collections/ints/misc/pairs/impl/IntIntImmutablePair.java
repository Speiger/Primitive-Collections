package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntIntPair;

/**
 * Mutable Pair Implementation that
 */
public class IntIntImmutablePair implements IntIntPair
{
	protected final int key;
	protected final int value;
	
	/**
	 * Default Constructor
	 */
	public IntIntImmutablePair() {
		this(0, 0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntIntImmutablePair(int key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntIntPair setIntKey(int key) {
		return new IntIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntIntPair setIntValue(int value) {
		return new IntIntImmutablePair(key, value);
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public IntIntPair set(int key, int value) {
		return new IntIntImmutablePair(key, value);
	}
	
	@Override
	public IntIntPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntIntPair) {
			IntIntPair entry = (IntIntPair)obj;
			return key == entry.getIntKey() && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Integer.toString(value);
	}
}