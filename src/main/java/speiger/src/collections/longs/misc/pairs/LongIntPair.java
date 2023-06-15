package speiger.src.collections.longs.misc.pairs;

import speiger.src.collections.longs.misc.pairs.impl.LongIntImmutablePair;
import speiger.src.collections.longs.misc.pairs.impl.LongIntMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface LongIntPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final LongIntPair EMPTY = new LongIntImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static LongIntPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static LongIntPair ofKey(long key) {
		return new LongIntImmutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static LongIntPair ofValue(int value) {
		return new LongIntImmutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static LongIntPair of(long key, int value) {
		return new LongIntImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static LongIntPair of(LongIntPair pair) {
		return new LongIntImmutablePair(pair.getLongKey(), pair.getIntValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static LongIntPair mutable() {
		return new LongIntMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static LongIntPair mutableKey(long key) {
		return new LongIntMutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static LongIntPair mutableValue(int value) {
		return new LongIntMutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static LongIntPair mutable(long key, int value) {
		return new LongIntMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static LongIntPair mutable(LongIntPair pair) {
		return new LongIntMutablePair(pair.getLongKey(), pair.getIntValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public LongIntPair setLongKey(long key);
	/**
	 * @return the Key of the Pair
	 */
	public long getLongKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public LongIntPair setIntValue(int value);
	
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
	public LongIntPair set(long key, int value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public LongIntPair shallowCopy();
}