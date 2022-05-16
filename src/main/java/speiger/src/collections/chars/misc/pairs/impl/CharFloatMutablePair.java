package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharFloatPair;



/**
 * Mutable Pair Implementation that
 */
public class CharFloatMutablePair implements CharFloatPair
{
	protected char key;
	protected float value;
	
	/**
	 * Default Constructor
	 */
	public CharFloatMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharFloatMutablePair(char key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharFloatPair setCharKey(char key) {
		this.key = key;
		return this;
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharFloatPair setFloatValue(float value) {
		this.value = value;
		return this;
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public CharFloatPair set(char key, float value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public CharFloatPair shallowCopy() {
		return CharFloatPair.mutable(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CharFloatPair) {
			CharFloatPair entry = (CharFloatPair)obj;
			return key == entry.getCharKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Character.hashCode(key) ^ Float.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Character.toString(key) + "->" + Float.toString(value);
	}
}