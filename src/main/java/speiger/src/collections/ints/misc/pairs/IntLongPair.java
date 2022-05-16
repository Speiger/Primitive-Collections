package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntLongImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntLongMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface IntLongPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntLongPair EMPTY = new IntLongImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static IntLongPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static IntLongPair ofKey(int key) {
		return new IntLongImmutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static IntLongPair ofValue(long value) {
		return new IntLongImmutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static IntLongPair of(int key, long value) {
		return new IntLongImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static IntLongPair of(IntLongPair pair) {
		return new IntLongImmutablePair(pair.getIntKey(), pair.getLongValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static IntLongPair mutable() {
		return new IntLongMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static IntLongPair mutableKey(int key) {
		return new IntLongMutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static IntLongPair mutableValue(long value) {
		return new IntLongMutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static IntLongPair mutable(int key, long value) {
		return new IntLongMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static IntLongPair mutable(IntLongPair pair) {
		return new IntLongMutablePair(pair.getIntKey(), pair.getLongValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntLongPair setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntLongPair setLongValue(long value);
	
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
	public IntLongPair set(int key, long value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntLongPair shallowCopy();
}