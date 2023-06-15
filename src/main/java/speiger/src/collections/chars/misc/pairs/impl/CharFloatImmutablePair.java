package speiger.src.collections.chars.misc.pairs.impl;


import speiger.src.collections.chars.misc.pairs.CharFloatPair;

/**
 * Mutable Pair Implementation that
 */
public class CharFloatImmutablePair implements CharFloatPair
{
	protected final char key;
	protected final float value;
	
	/**
	 * Default Constructor
	 */
	public CharFloatImmutablePair() {
		this((char)0, 0F);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public CharFloatImmutablePair(char key, float value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public CharFloatPair setCharKey(char key) {
		return new CharFloatImmutablePair(key, value);
	}
	
	@Override
	public char getCharKey() {
		return key;
	}
	
	@Override
	public CharFloatPair setFloatValue(float value) {
		return new CharFloatImmutablePair(key, value);
	}
	
	@Override
	public float getFloatValue() {
		return value;
	}
	
	@Override
	public CharFloatPair set(char key, float value) {
		return new CharFloatImmutablePair(key, value);
	}
	
	@Override
	public CharFloatPair shallowCopy() {
		return this;
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