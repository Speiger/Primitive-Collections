package speiger.src.collections.booleans.misc.pairs.impl;


import speiger.src.collections.booleans.misc.pairs.BooleanCharPair;

/**
 * Mutable Pair Implementation that
 */
public class BooleanCharImmutablePair implements BooleanCharPair
{
	protected final boolean key;
	protected final char value;
	
	/**
	 * Default Constructor
	 */
	public BooleanCharImmutablePair() {
		this(false, (char)0);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public BooleanCharImmutablePair(boolean key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BooleanCharPair setBooleanKey(boolean key) {
		return new BooleanCharImmutablePair(key, value);
	}
	
	@Override
	public boolean getBooleanKey() {
		return key;
	}
	
	@Override
	public BooleanCharPair setCharValue(char value) {
		return new BooleanCharImmutablePair(key, value);
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public BooleanCharPair set(boolean key, char value) {
		return new BooleanCharImmutablePair(key, value);
	}
	
	@Override
	public BooleanCharPair shallowCopy() {
		return this;
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