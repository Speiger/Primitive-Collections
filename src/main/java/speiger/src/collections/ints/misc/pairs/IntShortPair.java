package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntShortImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntShortMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface IntShortPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntShortPair EMPTY = new IntShortImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static IntShortPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static IntShortPair ofKey(int key) {
		return new IntShortImmutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static IntShortPair ofValue(short value) {
		return new IntShortImmutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static IntShortPair of(int key, short value) {
		return new IntShortImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static IntShortPair of(IntShortPair pair) {
		return new IntShortImmutablePair(pair.getIntKey(), pair.getShortValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static IntShortPair mutable() {
		return new IntShortMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static IntShortPair mutableKey(int key) {
		return new IntShortMutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static IntShortPair mutableValue(short value) {
		return new IntShortMutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static IntShortPair mutable(int key, short value) {
		return new IntShortMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static IntShortPair mutable(IntShortPair pair) {
		return new IntShortMutablePair(pair.getIntKey(), pair.getShortValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntShortPair setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntShortPair setShortValue(short value);
	
	/**
	 * @return the Value of the Pair
	 */
	public short getShortValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public IntShortPair set(int key, short value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntShortPair shallowCopy();
}