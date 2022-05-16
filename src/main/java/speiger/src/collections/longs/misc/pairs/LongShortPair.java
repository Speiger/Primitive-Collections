package speiger.src.collections.longs.misc.pairs;

import speiger.src.collections.longs.misc.pairs.impl.LongShortImmutablePair;
import speiger.src.collections.longs.misc.pairs.impl.LongShortMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface LongShortPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final LongShortPair EMPTY = new LongShortImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static LongShortPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static LongShortPair ofKey(long key) {
		return new LongShortImmutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static LongShortPair ofValue(short value) {
		return new LongShortImmutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static LongShortPair of(long key, short value) {
		return new LongShortImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static LongShortPair of(LongShortPair pair) {
		return new LongShortImmutablePair(pair.getLongKey(), pair.getShortValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static LongShortPair mutable() {
		return new LongShortMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static LongShortPair mutableKey(long key) {
		return new LongShortMutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static LongShortPair mutableValue(short value) {
		return new LongShortMutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static LongShortPair mutable(long key, short value) {
		return new LongShortMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static LongShortPair mutable(LongShortPair pair) {
		return new LongShortMutablePair(pair.getLongKey(), pair.getShortValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public LongShortPair setLongKey(long key);
	/**
	 * @return the Key of the Pair
	 */
	public long getLongKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public LongShortPair setShortValue(short value);
	
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
	public LongShortPair set(long key, short value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public LongShortPair shallowCopy();
}