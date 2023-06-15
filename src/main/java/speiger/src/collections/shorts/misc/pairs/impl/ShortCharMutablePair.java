package speiger.src.collections.shorts.misc.pairs.impl;


import speiger.src.collections.shorts.misc.pairs.ShortCharPair;



/**
 * Mutable Pair Implementation that
 */
public class ShortCharMutablePair implements ShortCharPair
{
	protected short key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public ShortCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ShortCharMutablePair(short key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ShortCharPair setShortKey(short key) {
		this.key = key;
		return this;
	}
	
	@Override
	public short getShortKey() {
		return key;
	}
	
	@Override
	public ShortCharPair setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public ShortCharPair set(short key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ShortCharPair shallowCopy() {
		return ShortCharPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ShortCharPair) {
			ShortCharPair entry = (ShortCharPair)obj;
			return key == entry.getShortKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Short.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Short.toString(key) + "->" + Character.toString(value);
	}
}