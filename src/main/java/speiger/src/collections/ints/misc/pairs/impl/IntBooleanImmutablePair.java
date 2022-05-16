package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntBooleanPair;

/**
 * Mutable Pair Implementation that
 */
public class IntBooleanImmutablePair implements IntBooleanPair
{
	protected final int key;
	protected final boolean value;
	
	/**
	 * Default Constructor
	 */
	public IntBooleanImmutablePair() {
		this(0, false);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntBooleanImmutablePair(int key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntBooleanPair setIntKey(int key) {
		return new IntBooleanImmutablePair(key, value);
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntBooleanPair setBooleanValue(boolean value) {
		return new IntBooleanImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public IntBooleanPair set(int key, boolean value) {
		return new IntBooleanImmutablePair(key, value);
	}
	
	@Override
	public IntBooleanPair shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntBooleanPair) {
			IntBooleanPair entry = (IntBooleanPair)obj;
			return key == entry.getIntKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Boolean.toString(value);
	}
}