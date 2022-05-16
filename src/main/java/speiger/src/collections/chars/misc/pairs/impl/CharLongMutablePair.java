package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharLongPair;



/**
 * Mutable Pair Implementation that
 */
public class CharLongMutablePair implements CharLongPair
{
	protected char key;
	protected long value;
	
	/**
	 * Default Constructor
	 */
	public CharLongMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharLongMutablePair(char key, long value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharLongPair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharLongPair setLongValue(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
	
	@Override
	public CharLongPair set(char key, long value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharLongPair shallowCopy() {
		return CharLongPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharLongPair) {
			CharLongPair entry = (CharLongPair)obj;
			return key == entry.getCharKey() && value == entry.getLongValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Long.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Long.toString(value);
	}
}