package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntBooleanPair;



/**
 * Mutable Pair Implementation that
 */
public class IntBooleanMutablePair implements IntBooleanPair
{
	protected int key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public IntBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntBooleanMutablePair(int key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntBooleanPair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntBooleanPair setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public IntBooleanPair set(int key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntBooleanPair shallowCopy() {
		return IntBooleanPair.mutable(key, value);
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