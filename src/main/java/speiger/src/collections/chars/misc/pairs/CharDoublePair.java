package speiger.src.collections.chars.misc.pairs;

import speiger.src.collections.chars.misc.pairs.impl.CharDoubleImmutablePair;
import speiger.src.collections.chars.misc.pairs.impl.CharDoubleMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface CharDoublePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final CharDoublePair EMPTY = new CharDoubleImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static CharDoublePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static CharDoublePair ofKey(char key) {
		return new CharDoubleImmutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static CharDoublePair ofValue(double value) {
		return new CharDoubleImmutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static CharDoublePair of(char key, double value) {
		return new CharDoubleImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static CharDoublePair of(CharDoublePair pair) {
		return new CharDoubleImmutablePair(pair.getCharKey(), pair.getDoubleValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static CharDoublePair mutable() {
		return new CharDoubleMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static CharDoublePair mutableKey(char key) {
		return new CharDoubleMutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static CharDoublePair mutableValue(double value) {
		return new CharDoubleMutablePair((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static CharDoublePair mutable(char key, double value) {
		return new CharDoubleMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static CharDoublePair mutable(CharDoublePair pair) {
		return new CharDoubleMutablePair(pair.getCharKey(), pair.getDoubleValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public CharDoublePair setCharKey(char key);
	/**
	 * @return the Key of the Pair
	 */
	public char getCharKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public CharDoublePair setDoubleValue(double value);
	
	/**
	 * @return the Value of the Pair
	 */
	public double getDoubleValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public CharDoublePair set(char key, double value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public CharDoublePair shallowCopy();
}