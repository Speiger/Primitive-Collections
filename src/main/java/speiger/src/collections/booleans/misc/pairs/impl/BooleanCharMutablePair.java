package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanCharPair;



/**
 * Mutable Pair Implementation that
 */
public class BooleanCharMutablePair implements BooleanCharPair
{
	protected boolean key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public BooleanCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanCharMutablePair(boolean key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanCharPair setBooleanKey(boolean key) {
		this.key = key;
		return this;
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanCharPair setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public BooleanCharPair set(boolean key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public BooleanCharPair shallowCopy() {
		return BooleanCharPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BooleanCharPair) {
			BooleanCharPair entry = (BooleanCharPair)obj;
			return key == entry.getBooleanKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Boolean.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Boolean.toString(key) + "->" + Character.toString(value);
	}
}