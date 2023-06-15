package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntFloatImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntFloatMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface IntFloatPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntFloatPair EMPTY = new IntFloatImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static IntFloatPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static IntFloatPair ofKey(int key) {
		return new IntFloatImmutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static IntFloatPair ofValue(float value) {
		return new IntFloatImmutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static IntFloatPair of(int key, float value) {
		return new IntFloatImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static IntFloatPair of(IntFloatPair pair) {
		return new IntFloatImmutablePair(pair.getIntKey(), pair.getFloatValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static IntFloatPair mutable() {
		return new IntFloatMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static IntFloatPair mutableKey(int key) {
		return new IntFloatMutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static IntFloatPair mutableValue(float value) {
		return new IntFloatMutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static IntFloatPair mutable(int key, float value) {
		return new IntFloatMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static IntFloatPair mutable(IntFloatPair pair) {
		return new IntFloatMutablePair(pair.getIntKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntFloatPair setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntFloatPair setFloatValue(float value);
	
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
	public IntFloatPair set(int key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntFloatPair shallowCopy();
}