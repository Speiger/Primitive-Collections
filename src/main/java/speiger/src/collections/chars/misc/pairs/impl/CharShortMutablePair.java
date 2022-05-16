package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharShortPair;



/**
 * Mutable Pair Implementation that
 */
public class CharShortMutablePair implements CharShortPair
{
	protected char key;
	protected short value;
	
	/**
	 * Default Constructor
	 */
	public CharShortMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharShortMutablePair(char key, short value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharShortPair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharShortPair setShortValue(short value) {
		this.value = value;
		return this;
	}
	
	@Override
	public short getShortValue() {
		return value;
	}
	
	@Override
	public CharShortPair set(char key, short value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharShortPair shallowCopy() {
		return CharShortPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharShortPair) {
			CharShortPair entry = (CharShortPair)obj;
			return key == entry.getCharKey() && value == entry.getShortValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Short.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Short.toString(value);
	}
}