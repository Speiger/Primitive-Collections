package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteBooleanImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteBooleanMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteBooleanPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteBooleanPair EMPTY = new ByteBooleanImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteBooleanPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteBooleanPair ofKey(byte key) {
		return new ByteBooleanImmutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteBooleanPair ofValue(boolean value) {
		return new ByteBooleanImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteBooleanPair of(byte key, boolean value) {
		return new ByteBooleanImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteBooleanPair of(ByteBooleanPair pair) {
		return new ByteBooleanImmutablePair(pair.getByteKey(), pair.getBooleanValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteBooleanPair mutable() {
		return new ByteBooleanMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteBooleanPair mutableKey(byte key) {
		return new ByteBooleanMutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteBooleanPair mutableValue(boolean value) {
		return new ByteBooleanMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteBooleanPair mutable(byte key, boolean value) {
		return new ByteBooleanMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteBooleanPair mutable(ByteBooleanPair pair) {
		return new ByteBooleanMutablePair(pair.getByteKey(), pair.getBooleanValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteBooleanPair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteBooleanPair setBooleanValue(boolean value);
	
	/**
	 * @return the Value of the Pair
	 */
	public boolean getBooleanValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ByteBooleanPair set(byte key, boolean value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteBooleanPair shallowCopy();
}