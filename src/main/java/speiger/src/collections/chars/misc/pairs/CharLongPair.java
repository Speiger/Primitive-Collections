package speiger.src.collections.chars.misc.pairs;

import speiger.src.collections.chars.misc.pairs.impl.CharLongImmutablePair;
import speiger.src.collections.chars.misc.pairs.impl.CharLongMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface CharLongPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final CharLongPair EMPTY = new CharLongImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static CharLongPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static CharLongPair ofKey(char key) {
		return new CharLongImmutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static CharLongPair ofValue(long value) {
		return new CharLongImmutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static CharLongPair of(char key, long value) {
		return new CharLongImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static CharLongPair of(CharLongPair pair) {
		return new CharLongImmutablePair(pair.getCharKey(), pair.getLongValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static CharLongPair mutable() {
		return new CharLongMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static CharLongPair mutableKey(char key) {
		return new CharLongMutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static CharLongPair mutableValue(long value) {
		return new CharLongMutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static CharLongPair mutable(char key, long value) {
		return new CharLongMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static CharLongPair mutable(CharLongPair pair) {
		return new CharLongMutablePair(pair.getCharKey(), pair.getLongValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public CharLongPair setCharKey(char key);
	/**
	 * @return the Key of the Pair
	 */
	public char getCharKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public CharLongPair setLongValue(long value);
	
	/**
	 * @return the Value of the Pair
	 */
	public long getLongValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public CharLongPair set(char key, long value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public CharLongPair shallowCopy();
}