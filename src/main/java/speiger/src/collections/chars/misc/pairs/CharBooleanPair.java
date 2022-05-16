package speiger.src.collections.chars.misc.pairs;

import speiger.src.collections.chars.misc.pairs.impl.CharBooleanImmutablePair;
import speiger.src.collections.chars.misc.pairs.impl.CharBooleanMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface CharBooleanPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final CharBooleanPair EMPTY = new CharBooleanImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static CharBooleanPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static CharBooleanPair ofKey(char key) {
		return new CharBooleanImmutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static CharBooleanPair ofValue(boolean value) {
		return new CharBooleanImmutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static CharBooleanPair of(char key, boolean value) {
		return new CharBooleanImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static CharBooleanPair of(CharBooleanPair pair) {
		return new CharBooleanImmutablePair(pair.getCharKey(), pair.getBooleanValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static CharBooleanPair mutable() {
		return new CharBooleanMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static CharBooleanPair mutableKey(char key) {
		return new CharBooleanMutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static CharBooleanPair mutableValue(boolean value) {
		return new CharBooleanMutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static CharBooleanPair mutable(char key, boolean value) {
		return new CharBooleanMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static CharBooleanPair mutable(CharBooleanPair pair) {
		return new CharBooleanMutablePair(pair.getCharKey(), pair.getBooleanValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public CharBooleanPair setCharKey(char key);
	/**
	 * @return the Key of the Pair
	 */
	public char getCharKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public CharBooleanPair setBooleanValue(boolean value);
	
	/**
	 * @return the Value of the Pair
	 */
	public boolean getBooleanValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public CharBooleanPair set(char key, boolean value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public CharBooleanPair shallowCopy();
}