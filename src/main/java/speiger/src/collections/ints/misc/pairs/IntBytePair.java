package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntByteImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntByteMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface IntBytePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntBytePair EMPTY = new IntByteImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static IntBytePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static IntBytePair ofKey(int key) {
		return new IntByteImmutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static IntBytePair ofValue(byte value) {
		return new IntByteImmutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static IntBytePair of(int key, byte value) {
		return new IntByteImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static IntBytePair of(IntBytePair pair) {
		return new IntByteImmutablePair(pair.getIntKey(), pair.getByteValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static IntBytePair mutable() {
		return new IntByteMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static IntBytePair mutableKey(int key) {
		return new IntByteMutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static IntBytePair mutableValue(byte value) {
		return new IntByteMutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static IntBytePair mutable(int key, byte value) {
		return new IntByteMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static IntBytePair mutable(IntBytePair pair) {
		return new IntByteMutablePair(pair.getIntKey(), pair.getByteValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntBytePair setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntBytePair setByteValue(byte value);
	
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
	public IntBytePair set(int key, byte value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntBytePair shallowCopy();
}