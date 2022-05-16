package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteShortImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteShortMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteShortPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteShortPair EMPTY = new ByteShortImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteShortPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteShortPair ofKey(byte key) {
		return new ByteShortImmutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteShortPair ofValue(short value) {
		return new ByteShortImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteShortPair of(byte key, short value) {
		return new ByteShortImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteShortPair of(ByteShortPair pair) {
		return new ByteShortImmutablePair(pair.getByteKey(), pair.getShortValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteShortPair mutable() {
		return new ByteShortMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteShortPair mutableKey(byte key) {
		return new ByteShortMutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteShortPair mutableValue(short value) {
		return new ByteShortMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteShortPair mutable(byte key, short value) {
		return new ByteShortMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteShortPair mutable(ByteShortPair pair) {
		return new ByteShortMutablePair(pair.getByteKey(), pair.getShortValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteShortPair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteShortPair setShortValue(short value);
	
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
	public ByteShortPair set(byte key, short value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteShortPair shallowCopy();
}