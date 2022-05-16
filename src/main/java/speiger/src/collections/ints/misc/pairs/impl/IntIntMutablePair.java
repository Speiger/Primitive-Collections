package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntIntPair;



/**
 * Mutable Pair Implementation that
 */
public class IntIntMutablePair implements IntIntPair
{
	protected int key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public IntIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntIntMutablePair(int key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntIntPair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public IntIntPair set(int key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntIntPair shallowCopy() {
		return IntIntPair.mutable(key, value);
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