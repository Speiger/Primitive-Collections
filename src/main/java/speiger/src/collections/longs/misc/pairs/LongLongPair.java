package speiger.src.collections.longs.misc.pairs;

import speiger.src.collections.longs.misc.pairs.impl.LongLongImmutablePair;
import speiger.src.collections.longs.misc.pairs.impl.LongLongMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface LongLongPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final LongLongPair EMPTY = new LongLongImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static LongLongPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static LongLongPair ofKey(long key) {
		return new LongLongImmutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static LongLongPair ofValue(long value) {
		return new LongLongImmutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static LongLongPair of(long key, long value) {
		return new LongLongImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static LongLongPair of(LongLongPair pair) {
		return new LongLongImmutablePair(pair.getLongKey(), pair.getLongValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static LongLongPair mutable() {
		return new LongLongMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static LongLongPair mutableKey(long key) {
		return new LongLongMutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static LongLongPair mutableValue(long value) {
		return new LongLongMutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static LongLongPair mutable(long key, long value) {
		return new LongLongMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static LongLongPair mutable(LongLongPair pair) {
		return new LongLongMutablePair(pair.getLongKey(), pair.getLongValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public LongLongPair setLongKey(long key);
	/**
	 * @return the Key of the Pair
	 */
	public long getLongKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public LongLongPair setLongValue(long value);
	
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
	public LongLongPair set(long key, long value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public LongLongPair shallowCopy();
}