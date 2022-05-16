package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteByteImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteByteMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteBytePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteBytePair EMPTY = new ByteByteImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteBytePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteBytePair ofKey(byte key) {
		return new ByteByteImmutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteBytePair ofValue(byte value) {
		return new ByteByteImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteBytePair of(byte key, byte value) {
		return new ByteByteImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteBytePair of(ByteBytePair pair) {
		return new ByteByteImmutablePair(pair.getByteKey(), pair.getByteValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteBytePair mutable() {
		return new ByteByteMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteBytePair mutableKey(byte key) {
		return new ByteByteMutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteBytePair mutableValue(byte value) {
		return new ByteByteMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteBytePair mutable(byte key, byte value) {
		return new ByteByteMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteBytePair mutable(ByteBytePair pair) {
		return new ByteByteMutablePair(pair.getByteKey(), pair.getByteValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteBytePair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteBytePair setByteValue(byte value);
	
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
	public ByteBytePair set(byte key, byte value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteBytePair shallowCopy();
}