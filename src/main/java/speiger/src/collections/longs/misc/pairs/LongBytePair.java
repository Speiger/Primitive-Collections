package speiger.src.collections.longs.misc.pairs;

import speiger.src.collections.longs.misc.pairs.impl.LongByteImmutablePair;
import speiger.src.collections.longs.misc.pairs.impl.LongByteMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface LongBytePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final LongBytePair EMPTY = new LongByteImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static LongBytePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static LongBytePair ofKey(long key) {
		return new LongByteImmutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static LongBytePair ofValue(byte value) {
		return new LongByteImmutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static LongBytePair of(long key, byte value) {
		return new LongByteImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static LongBytePair of(LongBytePair pair) {
		return new LongByteImmutablePair(pair.getLongKey(), pair.getByteValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static LongBytePair mutable() {
		return new LongByteMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static LongBytePair mutableKey(long key) {
		return new LongByteMutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static LongBytePair mutableValue(byte value) {
		return new LongByteMutablePair(0L, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static LongBytePair mutable(long key, byte value) {
		return new LongByteMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static LongBytePair mutable(LongBytePair pair) {
		return new LongByteMutablePair(pair.getLongKey(), pair.getByteValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public LongBytePair setLongKey(long key);
	/**
	 * @return the Key of the Pair
	 */
	public long getLongKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public LongBytePair setByteValue(byte value);
	
	/**
	 * @return the Value of the Pair
	 */
	public byte getByteValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public LongBytePair set(long key, byte value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public LongBytePair shallowCopy();
}