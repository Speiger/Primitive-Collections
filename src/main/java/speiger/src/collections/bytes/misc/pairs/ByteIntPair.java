package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteIntImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteIntMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteIntPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteIntPair EMPTY = new ByteIntImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteIntPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteIntPair ofKey(byte key) {
		return new ByteIntImmutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteIntPair ofValue(int value) {
		return new ByteIntImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteIntPair of(byte key, int value) {
		return new ByteIntImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteIntPair of(ByteIntPair pair) {
		return new ByteIntImmutablePair(pair.getByteKey(), pair.getIntValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteIntPair mutable() {
		return new ByteIntMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteIntPair mutableKey(byte key) {
		return new ByteIntMutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteIntPair mutableValue(int value) {
		return new ByteIntMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteIntPair mutable(byte key, int value) {
		return new ByteIntMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteIntPair mutable(ByteIntPair pair) {
		return new ByteIntMutablePair(pair.getByteKey(), pair.getIntValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteIntPair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteIntPair setIntValue(int value);
	
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
	public ByteIntPair set(byte key, int value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteIntPair shallowCopy();
}