package speiger.src.collections.floats.misc.pairs;

import speiger.src.collections.floats.misc.pairs.impl.FloatBooleanImmutablePair;
import speiger.src.collections.floats.misc.pairs.impl.FloatBooleanMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface FloatBooleanPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final FloatBooleanPair EMPTY = new FloatBooleanImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static FloatBooleanPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static FloatBooleanPair ofKey(float key) {
		return new FloatBooleanImmutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static FloatBooleanPair ofValue(boolean value) {
		return new FloatBooleanImmutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static FloatBooleanPair of(float key, boolean value) {
		return new FloatBooleanImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static FloatBooleanPair of(FloatBooleanPair pair) {
		return new FloatBooleanImmutablePair(pair.getFloatKey(), pair.getBooleanValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static FloatBooleanPair mutable() {
		return new FloatBooleanMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static FloatBooleanPair mutableKey(float key) {
		return new FloatBooleanMutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static FloatBooleanPair mutableValue(boolean value) {
		return new FloatBooleanMutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static FloatBooleanPair mutable(float key, boolean value) {
		return new FloatBooleanMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static FloatBooleanPair mutable(FloatBooleanPair pair) {
		return new FloatBooleanMutablePair(pair.getFloatKey(), pair.getBooleanValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public FloatBooleanPair setFloatKey(float key);
	/**
	 * @return the Key of the Pair
	 */
	public float getFloatKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public FloatBooleanPair setBooleanValue(boolean value);
	
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
	public FloatBooleanPair set(float key, boolean value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public FloatBooleanPair shallowCopy();
}