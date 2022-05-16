package speiger.src.collections.floats.misc.pairs;

import speiger.src.collections.floats.misc.pairs.impl.FloatFloatImmutablePair;
import speiger.src.collections.floats.misc.pairs.impl.FloatFloatMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface FloatFloatPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final FloatFloatPair EMPTY = new FloatFloatImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static FloatFloatPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static FloatFloatPair ofKey(float key) {
		return new FloatFloatImmutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static FloatFloatPair ofValue(float value) {
		return new FloatFloatImmutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static FloatFloatPair of(float key, float value) {
		return new FloatFloatImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static FloatFloatPair of(FloatFloatPair pair) {
		return new FloatFloatImmutablePair(pair.getFloatKey(), pair.getFloatValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static FloatFloatPair mutable() {
		return new FloatFloatMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static FloatFloatPair mutableKey(float key) {
		return new FloatFloatMutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static FloatFloatPair mutableValue(float value) {
		return new FloatFloatMutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static FloatFloatPair mutable(float key, float value) {
		return new FloatFloatMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static FloatFloatPair mutable(FloatFloatPair pair) {
		return new FloatFloatMutablePair(pair.getFloatKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public FloatFloatPair setFloatKey(float key);
	/**
	 * @return the Key of the Pair
	 */
	public float getFloatKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public FloatFloatPair setFloatValue(float value);
	
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
	public FloatFloatPair set(float key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public FloatFloatPair shallowCopy();
}