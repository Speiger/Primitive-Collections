package speiger.src.collections.shorts.misc.pairs;

import speiger.src.collections.shorts.misc.pairs.impl.ShortBooleanImmutablePair;
import speiger.src.collections.shorts.misc.pairs.impl.ShortBooleanMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ShortBooleanPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ShortBooleanPair EMPTY = new ShortBooleanImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ShortBooleanPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ShortBooleanPair ofKey(short key) {
		return new ShortBooleanImmutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ShortBooleanPair ofValue(boolean value) {
		return new ShortBooleanImmutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ShortBooleanPair of(short key, boolean value) {
		return new ShortBooleanImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ShortBooleanPair of(ShortBooleanPair pair) {
		return new ShortBooleanImmutablePair(pair.getShortKey(), pair.getBooleanValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ShortBooleanPair mutable() {
		return new ShortBooleanMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ShortBooleanPair mutableKey(short key) {
		return new ShortBooleanMutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ShortBooleanPair mutableValue(boolean value) {
		return new ShortBooleanMutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ShortBooleanPair mutable(short key, boolean value) {
		return new ShortBooleanMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ShortBooleanPair mutable(ShortBooleanPair pair) {
		return new ShortBooleanMutablePair(pair.getShortKey(), pair.getBooleanValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ShortBooleanPair setShortKey(short key);
	/**
	 * @return the Key of the Pair
	 */
	public short getShortKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ShortBooleanPair setBooleanValue(boolean value);
	
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
	public ShortBooleanPair set(short key, boolean value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ShortBooleanPair shallowCopy();
}