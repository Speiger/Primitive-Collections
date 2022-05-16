package speiger.src.collections.ints.misc.pairs.impl;


import speiger.src.collections.ints.misc.pairs.IntCharPair;



/**
 * Mutable Pair Implementation that
 */
public class IntCharMutablePair implements IntCharPair
{
	protected int key;
	protected char value;
	
	/**
	 * Default Constructor
	 */
	public IntCharMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public IntCharMutablePair(int key, char value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public IntCharPair setIntKey(int key) {
		this.key = key;
		return this;
	}
	
	@Override
	public int getIntKey() {
		return key;
	}
	
	@Override
	public IntCharPair setCharValue(char value) {
		this.value = value;
		return this;
	}
	
	@Override
	public char getCharValue() {
		return value;
	}
	
	@Override
	public IntCharPair set(int key, char value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public IntCharPair shallowCopy() {
		return IntCharPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntCharPair) {
			IntCharPair entry = (IntCharPair)obj;
			return key == entry.getIntKey() && value == entry.getCharValue();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(key) ^ Character.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Integer.toString(key) + "->" + Character.toString(value);
	}
}