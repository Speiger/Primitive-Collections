package speiger.src.collections.booleans.misc.pairs;

import speiger.src.collections.booleans.misc.pairs.impl.BooleanByteImmutablePair;
import speiger.src.collections.booleans.misc.pairs.impl.BooleanByteMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface BooleanBytePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final BooleanBytePair EMPTY = new BooleanByteImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static BooleanBytePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static BooleanBytePair ofKey(boolean key) {
		return new BooleanByteImmutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static BooleanBytePair ofValue(byte value) {
		return new BooleanByteImmutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static BooleanBytePair of(boolean key, byte value) {
		return new BooleanByteImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static BooleanBytePair of(BooleanBytePair pair) {
		return new BooleanByteImmutablePair(pair.getBooleanKey(), pair.getByteValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static BooleanBytePair mutable() {
		return new BooleanByteMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static BooleanBytePair mutableKey(boolean key) {
		return new BooleanByteMutablePair(key, (byte)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static BooleanBytePair mutableValue(byte value) {
		return new BooleanByteMutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static BooleanBytePair mutable(boolean key, byte value) {
		return new BooleanByteMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static BooleanBytePair mutable(BooleanBytePair pair) {
		return new BooleanByteMutablePair(pair.getBooleanKey(), pair.getByteValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public BooleanBytePair setBooleanKey(boolean key);
	/**
	 * @return the Key of the Pair
	 */
	public boolean getBooleanKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public BooleanBytePair setByteValue(byte value);
	
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
	public BooleanBytePair set(boolean key, byte value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public BooleanBytePair shallowCopy();
}