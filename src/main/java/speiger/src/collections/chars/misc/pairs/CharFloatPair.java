package speiger.src.collections.chars.misc.pairs;

import speiger.src.collections.chars.misc.pairs.impl.CharFloatImmutablePair;
import speiger.src.collections.chars.misc.pairs.impl.CharFloatMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface CharFloatPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final CharFloatPair EMPTY = new CharFloatImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static CharFloatPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static CharFloatPair ofKey(char key) {
		return new CharFloatImmutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static CharFloatPair ofValue(float value) {
		return new CharFloatImmutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static CharFloatPair of(char key, float value) {
		return new CharFloatImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static CharFloatPair of(CharFloatPair pair) {
		return new CharFloatImmutablePair(pair.getCharKey(), pair.getFloatValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static CharFloatPair mutable() {
		return new CharFloatMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static CharFloatPair mutableKey(char key) {
		return new CharFloatMutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static CharFloatPair mutableValue(float value) {
		return new CharFloatMutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static CharFloatPair mutable(char key, float value) {
		return new CharFloatMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static CharFloatPair mutable(CharFloatPair pair) {
		return new CharFloatMutablePair(pair.getCharKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public CharFloatPair setCharKey(char key);
	/**
	 * @return the Key of the Pair
	 */
	public char getCharKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public CharFloatPair setFloatValue(float value);
	
	/**
	 * @return the Value of the Pair
	 */
	public float getFloatValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public CharFloatPair set(char key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public CharFloatPair shallowCopy();
}