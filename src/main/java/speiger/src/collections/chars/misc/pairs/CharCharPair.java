package speiger.src.collections.chars.misc.pairs;

import speiger.src.collections.chars.misc.pairs.impl.CharCharImmutablePair;
import speiger.src.collections.chars.misc.pairs.impl.CharCharMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface CharCharPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final CharCharPair EMPTY = new CharCharImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static CharCharPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static CharCharPair ofKey(char key) {
		return new CharCharImmutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static CharCharPair ofValue(char value) {
		return new CharCharImmutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static CharCharPair of(char key, char value) {
		return new CharCharImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static CharCharPair of(CharCharPair pair) {
		return new CharCharImmutablePair(pair.getCharKey(), pair.getCharValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static CharCharPair mutable() {
		return new CharCharMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static CharCharPair mutableKey(char key) {
		return new CharCharMutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static CharCharPair mutableValue(char value) {
		return new CharCharMutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static CharCharPair mutable(char key, char value) {
		return new CharCharMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static CharCharPair mutable(CharCharPair pair) {
		return new CharCharMutablePair(pair.getCharKey(), pair.getCharValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public CharCharPair setCharKey(char key);
	/**
	 * @return the Key of the Pair
	 */
	public char getCharKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public CharCharPair setCharValue(char value);
	
	/**
	 * @return the Value of the Pair
	 */
	public char getCharValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public CharCharPair set(char key, char value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public CharCharPair shallowCopy();
}