package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntCharImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntCharMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface IntCharPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntCharPair EMPTY = new IntCharImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static IntCharPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static IntCharPair ofKey(int key) {
		return new IntCharImmutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static IntCharPair ofValue(char value) {
		return new IntCharImmutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static IntCharPair of(int key, char value) {
		return new IntCharImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static IntCharPair of(IntCharPair pair) {
		return new IntCharImmutablePair(pair.getIntKey(), pair.getCharValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static IntCharPair mutable() {
		return new IntCharMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static IntCharPair mutableKey(int key) {
		return new IntCharMutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static IntCharPair mutableValue(char value) {
		return new IntCharMutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static IntCharPair mutable(int key, char value) {
		return new IntCharMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static IntCharPair mutable(IntCharPair pair) {
		return new IntCharMutablePair(pair.getIntKey(), pair.getCharValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntCharPair setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntCharPair setCharValue(char value);
	
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
	public IntCharPair set(int key, char value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntCharPair shallowCopy();
}