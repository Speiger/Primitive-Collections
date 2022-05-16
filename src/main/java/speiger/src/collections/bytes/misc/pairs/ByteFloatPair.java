package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteFloatImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteFloatMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteFloatPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteFloatPair EMPTY = new ByteFloatImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteFloatPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteFloatPair ofKey(byte key) {
		return new ByteFloatImmutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteFloatPair ofValue(float value) {
		return new ByteFloatImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteFloatPair of(byte key, float value) {
		return new ByteFloatImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteFloatPair of(ByteFloatPair pair) {
		return new ByteFloatImmutablePair(pair.getByteKey(), pair.getFloatValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteFloatPair mutable() {
		return new ByteFloatMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteFloatPair mutableKey(byte key) {
		return new ByteFloatMutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteFloatPair mutableValue(float value) {
		return new ByteFloatMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteFloatPair mutable(byte key, float value) {
		return new ByteFloatMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteFloatPair mutable(ByteFloatPair pair) {
		return new ByteFloatMutablePair(pair.getByteKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteFloatPair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteFloatPair setFloatValue(float value);
	
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
	public ByteFloatPair set(byte key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteFloatPair shallowCopy();
}