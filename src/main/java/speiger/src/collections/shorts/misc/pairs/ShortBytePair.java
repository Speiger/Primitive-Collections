package speiger.src.collections.shorts.misc.pairs;

import speiger.src.collections.shorts.misc.pairs.impl.ShortByteImmutablePair;
import speiger.src.collections.shorts.misc.pairs.impl.ShortByteMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ShortBytePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ShortBytePair EMPTY = new ShortByteImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ShortBytePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ShortBytePair ofKey(short key) {
		return new ShortByteImmutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ShortBytePair ofValue(byte value) {
		return new ShortByteImmutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ShortBytePair of(short key, byte value) {
		return new ShortByteImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ShortBytePair of(ShortBytePair pair) {
		return new ShortByteImmutablePair(pair.getShortKey(), pair.getByteValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ShortBytePair mutable() {
		return new ShortByteMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ShortBytePair mutableKey(short key) {
		return new ShortByteMutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ShortBytePair mutableValue(byte value) {
		return new ShortByteMutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ShortBytePair mutable(short key, byte value) {
		return new ShortByteMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ShortBytePair mutable(ShortBytePair pair) {
		return new ShortByteMutablePair(pair.getShortKey(), pair.getByteValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ShortBytePair setShortKey(short key);
	/**
	 * @return the Key of the Pair
	 */
	public short getShortKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ShortBytePair setByteValue(byte value);
	
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
	public ShortBytePair set(short key, byte value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ShortBytePair shallowCopy();
}