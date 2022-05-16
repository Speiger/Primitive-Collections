package speiger.src.collections.chars.misc.pairs;

import speiger.src.collections.chars.misc.pairs.impl.CharIntImmutablePair;
import speiger.src.collections.chars.misc.pairs.impl.CharIntMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface CharIntPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final CharIntPair EMPTY = new CharIntImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static CharIntPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static CharIntPair ofKey(char key) {
		return new CharIntImmutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static CharIntPair ofValue(int value) {
		return new CharIntImmutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static CharIntPair of(char key, int value) {
		return new CharIntImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static CharIntPair of(CharIntPair pair) {
		return new CharIntImmutablePair(pair.getCharKey(), pair.getIntValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static CharIntPair mutable() {
		return new CharIntMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static CharIntPair mutableKey(char key) {
		return new CharIntMutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static CharIntPair mutableValue(int value) {
		return new CharIntMutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static CharIntPair mutable(char key, int value) {
		return new CharIntMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static CharIntPair mutable(CharIntPair pair) {
		return new CharIntMutablePair(pair.getCharKey(), pair.getIntValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public CharIntPair setCharKey(char key);
	/**
	 * @return the Key of the Pair
	 */
	public char getCharKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public CharIntPair setIntValue(int value);
	
	/**
	 * @return the Value of the Pair
	 */
	public int getIntValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public CharIntPair set(char key, int value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public CharIntPair shallowCopy();
}