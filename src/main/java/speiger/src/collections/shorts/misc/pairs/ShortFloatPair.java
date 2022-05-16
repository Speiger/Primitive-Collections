package speiger.src.collections.shorts.misc.pairs;

import speiger.src.collections.shorts.misc.pairs.impl.ShortFloatImmutablePair;
import speiger.src.collections.shorts.misc.pairs.impl.ShortFloatMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ShortFloatPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ShortFloatPair EMPTY = new ShortFloatImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ShortFloatPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ShortFloatPair ofKey(short key) {
		return new ShortFloatImmutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ShortFloatPair ofValue(float value) {
		return new ShortFloatImmutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ShortFloatPair of(short key, float value) {
		return new ShortFloatImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ShortFloatPair of(ShortFloatPair pair) {
		return new ShortFloatImmutablePair(pair.getShortKey(), pair.getFloatValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ShortFloatPair mutable() {
		return new ShortFloatMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ShortFloatPair mutableKey(short key) {
		return new ShortFloatMutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ShortFloatPair mutableValue(float value) {
		return new ShortFloatMutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ShortFloatPair mutable(short key, float value) {
		return new ShortFloatMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ShortFloatPair mutable(ShortFloatPair pair) {
		return new ShortFloatMutablePair(pair.getShortKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ShortFloatPair setShortKey(short key);
	/**
	 * @return the Key of the Pair
	 */
	public short getShortKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ShortFloatPair setFloatValue(float value);
	
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
	public ShortFloatPair set(short key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ShortFloatPair shallowCopy();
}