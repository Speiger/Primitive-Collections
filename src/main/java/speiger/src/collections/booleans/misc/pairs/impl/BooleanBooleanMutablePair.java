package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanBooleanPair;



/**
 * Mutable Pair Implementation that
 */
public class BooleanBooleanMutablePair implements BooleanBooleanPair
{
	protected boolean key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public BooleanBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanBooleanMutablePair(boolean key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanBooleanPair setBooleanKey(boolean key) {
		this.key = key;
		return this;
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanBooleanPair setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public BooleanBooleanPair set(boolean key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public BooleanBooleanPair shallowCopy() {
		return BooleanBooleanPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanBooleanPair) {
			BooleanBooleanPair entry = (BooleanBooleanPair)obj;
			return key == entry.getBooleanKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Boolean.toString(value);
	}
}