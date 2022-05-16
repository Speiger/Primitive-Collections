package speiger.src.collections.longs.misc.pairs;

import speiger.src.collections.longs.misc.pairs.impl.LongFloatImmutablePair;
import speiger.src.collections.longs.misc.pairs.impl.LongFloatMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface LongFloatPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final LongFloatPair EMPTY = new LongFloatImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static LongFloatPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static LongFloatPair ofKey(long key) {
		return new LongFloatImmutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static LongFloatPair ofValue(float value) {
		return new LongFloatImmutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static LongFloatPair of(long key, float value) {
		return new LongFloatImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static LongFloatPair of(LongFloatPair pair) {
		return new LongFloatImmutablePair(pair.getLongKey(), pair.getFloatValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static LongFloatPair mutable() {
		return new LongFloatMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static LongFloatPair mutableKey(long key) {
		return new LongFloatMutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static LongFloatPair mutableValue(float value) {
		return new LongFloatMutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static LongFloatPair mutable(long key, float value) {
		return new LongFloatMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static LongFloatPair mutable(LongFloatPair pair) {
		return new LongFloatMutablePair(pair.getLongKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public LongFloatPair setLongKey(long key);
	/**
	 * @return the Key of the Pair
	 */
	public long getLongKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public LongFloatPair setFloatValue(float value);
	
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
	public LongFloatPair set(long key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public LongFloatPair shallowCopy();
}