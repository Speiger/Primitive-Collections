package speiger.src.collections.shorts.misc.pairs;

import speiger.src.collections.shorts.misc.pairs.impl.ShortIntImmutablePair;
import speiger.src.collections.shorts.misc.pairs.impl.ShortIntMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ShortIntPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ShortIntPair EMPTY = new ShortIntImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ShortIntPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ShortIntPair ofKey(short key) {
		return new ShortIntImmutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ShortIntPair ofValue(int value) {
		return new ShortIntImmutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ShortIntPair of(short key, int value) {
		return new ShortIntImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ShortIntPair of(ShortIntPair pair) {
		return new ShortIntImmutablePair(pair.getShortKey(), pair.getIntValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ShortIntPair mutable() {
		return new ShortIntMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ShortIntPair mutableKey(short key) {
		return new ShortIntMutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ShortIntPair mutableValue(int value) {
		return new ShortIntMutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ShortIntPair mutable(short key, int value) {
		return new ShortIntMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ShortIntPair mutable(ShortIntPair pair) {
		return new ShortIntMutablePair(pair.getShortKey(), pair.getIntValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ShortIntPair setShortKey(short key);
	/**
	 * @return the Key of the Pair
	 */
	public short getShortKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ShortIntPair setIntValue(int value);
	
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
	public ShortIntPair set(short key, int value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ShortIntPair shallowCopy();
}