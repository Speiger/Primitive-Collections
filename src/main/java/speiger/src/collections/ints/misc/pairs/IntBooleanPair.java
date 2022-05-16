package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntBooleanImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntBooleanMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface IntBooleanPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntBooleanPair EMPTY = new IntBooleanImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static IntBooleanPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static IntBooleanPair ofKey(int key) {
		return new IntBooleanImmutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static IntBooleanPair ofValue(boolean value) {
		return new IntBooleanImmutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static IntBooleanPair of(int key, boolean value) {
		return new IntBooleanImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static IntBooleanPair of(IntBooleanPair pair) {
		return new IntBooleanImmutablePair(pair.getIntKey(), pair.getBooleanValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static IntBooleanPair mutable() {
		return new IntBooleanMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static IntBooleanPair mutableKey(int key) {
		return new IntBooleanMutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static IntBooleanPair mutableValue(boolean value) {
		return new IntBooleanMutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static IntBooleanPair mutable(int key, boolean value) {
		return new IntBooleanMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static IntBooleanPair mutable(IntBooleanPair pair) {
		return new IntBooleanMutablePair(pair.getIntKey(), pair.getBooleanValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntBooleanPair setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntBooleanPair setBooleanValue(boolean value);
	
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
	public IntBooleanPair set(int key, boolean value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntBooleanPair shallowCopy();
}