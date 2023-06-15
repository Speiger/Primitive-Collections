package speiger.src.collections.booleans.misc.pairs;

import speiger.src.collections.booleans.misc.pairs.impl.BooleanLongImmutablePair;
import speiger.src.collections.booleans.misc.pairs.impl.BooleanLongMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface BooleanLongPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final BooleanLongPair EMPTY = new BooleanLongImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static BooleanLongPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static BooleanLongPair ofKey(boolean key) {
		return new BooleanLongImmutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static BooleanLongPair ofValue(long value) {
		return new BooleanLongImmutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static BooleanLongPair of(boolean key, long value) {
		return new BooleanLongImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static BooleanLongPair of(BooleanLongPair pair) {
		return new BooleanLongImmutablePair(pair.getBooleanKey(), pair.getLongValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static BooleanLongPair mutable() {
		return new BooleanLongMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static BooleanLongPair mutableKey(boolean key) {
		return new BooleanLongMutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static BooleanLongPair mutableValue(long value) {
		return new BooleanLongMutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static BooleanLongPair mutable(boolean key, long value) {
		return new BooleanLongMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static BooleanLongPair mutable(BooleanLongPair pair) {
		return new BooleanLongMutablePair(pair.getBooleanKey(), pair.getLongValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public BooleanLongPair setBooleanKey(boolean key);
	/**
	 * @return the Key of the Pair
	 */
	public boolean getBooleanKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public BooleanLongPair setLongValue(long value);
	
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
	public BooleanLongPair set(boolean key, long value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public BooleanLongPair shallowCopy();
}