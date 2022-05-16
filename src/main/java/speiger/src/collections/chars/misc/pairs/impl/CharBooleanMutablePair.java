package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharBooleanPair;



/**
 * Mutable Pair Implementation that
 */
public class CharBooleanMutablePair implements CharBooleanPair
{
	protected char key;
	protected boolean value;
	
	/**
	 * Default Constructor
	 */
	public CharBooleanMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharBooleanMutablePair(char key, boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharBooleanPair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharBooleanPair setBooleanValue(boolean value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean getBooleanValue() {
		return value;
	}
	
	@Override
	public CharBooleanPair set(char key, boolean value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharBooleanPair shallowCopy() {
		return CharBooleanPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharBooleanPair) {
			CharBooleanPair entry = (CharBooleanPair)obj;
			return key == entry.getCharKey() && value == entry.getBooleanValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Boolean.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Boolean.toString(value);
	}
}