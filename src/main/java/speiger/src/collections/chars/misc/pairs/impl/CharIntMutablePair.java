package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharIntPair;



/**
 * Mutable Pair Implementation that
 */
public class CharIntMutablePair implements CharIntPair
{
	protected char key;
	protected int value;
	
	/**
	 * Default Constructor
	 */
	public CharIntMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharIntMutablePair(char key, int value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharIntPair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharIntPair setIntValue(int value) {
		this.value = value;
		return this;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}
	
	@Override
	public CharIntPair set(char key, int value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharIntPair shallowCopy() {
		return CharIntPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharIntPair) {
			CharIntPair entry = (CharIntPair)obj;
			return key == entry.getCharKey() && value == entry.getIntValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Integer.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Integer.toString(value);
	}
}