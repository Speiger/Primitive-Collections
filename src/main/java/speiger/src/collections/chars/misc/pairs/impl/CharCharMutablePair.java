package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharCharPair;



/**
 * Mutable Pair Implementation that
 */
public class CharCharMutablePair implements CharCharPair
{
	protected char key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public CharCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharCharMutablePair(char key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharCharPair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharCharPair setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public CharCharPair set(char key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharCharPair shallowCopy() {
		return CharCharPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharCharPair) {
			CharCharPair entry = (CharCharPair)obj;
			return key == entry.getCharKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Character.toString(value);
	}
}