package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanIntPair;



/**
 * Mutable Pair Implementation that
 */
public class BooleanIntMutablePair implements BooleanIntPair
{
	protected boolean key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public BooleanIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanIntMutablePair(boolean key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanIntPair setBooleanKey(boolean key) {
		this.key = key;
		return this;
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public BooleanIntPair set(boolean key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public BooleanIntPair shallowCopy() {
		return BooleanIntPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanIntPair) {
			BooleanIntPair entry = (BooleanIntPair)obj;
			return key == entry.getBooleanKey() && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Integer.toString(value);
	}
}