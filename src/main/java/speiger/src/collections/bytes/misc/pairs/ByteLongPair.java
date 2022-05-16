package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteLongImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteLongMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteLongPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteLongPair EMPTY = new ByteLongImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteLongPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteLongPair ofKey(byte key) {
		return new ByteLongImmutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteLongPair ofValue(long value) {
		return new ByteLongImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteLongPair of(byte key, long value) {
		return new ByteLongImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteLongPair of(ByteLongPair pair) {
		return new ByteLongImmutablePair(pair.getByteKey(), pair.getLongValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteLongPair mutable() {
		return new ByteLongMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteLongPair mutableKey(byte key) {
		return new ByteLongMutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteLongPair mutableValue(long value) {
		return new ByteLongMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteLongPair mutable(byte key, long value) {
		return new ByteLongMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteLongPair mutable(ByteLongPair pair) {
		return new ByteLongMutablePair(pair.getByteKey(), pair.getLongValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteLongPair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteLongPair setLongValue(long value);
	
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
	public ByteLongPair set(byte key, long value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteLongPair shallowCopy();
}