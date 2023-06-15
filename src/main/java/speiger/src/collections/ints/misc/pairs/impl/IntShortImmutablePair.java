package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntShortPair;

/**
 * Mutable Pair Implementation that
 */
public class IntShortImmutablePair implements IntShortPair
{
	protected final int key;
	protected final short value;
	
	/**
	 * Default Constructor
	 */
	public IntShortImmutablePair() {
		this(0, (short)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntShortImmutablePair(int key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntShortPair setIntKey(int key) {
		return new IntShortImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntShortPair setShortValue(short value) {
		return new IntShortImmutablePair(key, value);
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public IntShortPair set(int key, short value) {
		return new IntShortImmutablePair(key, value);
	}
	
	@Override
	public IntShortPair shallowCopy() {
		return this;
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