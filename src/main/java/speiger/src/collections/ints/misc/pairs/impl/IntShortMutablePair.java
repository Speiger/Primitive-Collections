package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntShortPair;



/**
 * Mutable Pair Implementation that
 */
public class IntShortMutablePair implements IntShortPair
{
	protected int key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public IntShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntShortMutablePair(int key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntShortPair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntShortPair setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public IntShortPair set(int key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntShortPair shallowCopy() {
		return IntShortPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntShortPair) {
			IntShortPair entry = (IntShortPair)obj;
			return key == entry.getIntKey() && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Short.toString(value);
	}
}