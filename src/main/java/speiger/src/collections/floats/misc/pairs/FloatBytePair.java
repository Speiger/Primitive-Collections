package speiger.src.collections.floats.misc.pairs;

import speiger.src.collections.floats.misc.pairs.impl.FloatByteImmutablePair;
import speiger.src.collections.floats.misc.pairs.impl.FloatByteMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface FloatBytePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final FloatBytePair EMPTY = new FloatByteImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static FloatBytePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static FloatBytePair ofKey(float key) {
		return new FloatByteImmutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static FloatBytePair ofValue(byte value) {
		return new FloatByteImmutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static FloatBytePair of(float key, byte value) {
		return new FloatByteImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static FloatBytePair of(FloatBytePair pair) {
		return new FloatByteImmutablePair(pair.getFloatKey(), pair.getByteValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static FloatBytePair mutable() {
		return new FloatByteMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static FloatBytePair mutableKey(float key) {
		return new FloatByteMutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static FloatBytePair mutableValue(byte value) {
		return new FloatByteMutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static FloatBytePair mutable(float key, byte value) {
		return new FloatByteMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static FloatBytePair mutable(FloatBytePair pair) {
		return new FloatByteMutablePair(pair.getFloatKey(), pair.getByteValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public FloatBytePair setFloatKey(float key);
	/**
	 * @return the Key of the Pair
	 */
	public float getFloatKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public FloatBytePair setByteValue(byte value);
	
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
	public FloatBytePair set(float key, byte value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public FloatBytePair shallowCopy();
}