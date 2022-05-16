package speiger.src.collections.floats.misc.pairs;

import speiger.src.collections.floats.misc.pairs.impl.FloatDoubleImmutablePair;
import speiger.src.collections.floats.misc.pairs.impl.FloatDoubleMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface FloatDoublePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final FloatDoublePair EMPTY = new FloatDoubleImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static FloatDoublePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static FloatDoublePair ofKey(float key) {
		return new FloatDoubleImmutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static FloatDoublePair ofValue(double value) {
		return new FloatDoubleImmutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static FloatDoublePair of(float key, double value) {
		return new FloatDoubleImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static FloatDoublePair of(FloatDoublePair pair) {
		return new FloatDoubleImmutablePair(pair.getFloatKey(), pair.getDoubleValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static FloatDoublePair mutable() {
		return new FloatDoubleMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static FloatDoublePair mutableKey(float key) {
		return new FloatDoubleMutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static FloatDoublePair mutableValue(double value) {
		return new FloatDoubleMutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static FloatDoublePair mutable(float key, double value) {
		return new FloatDoubleMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static FloatDoublePair mutable(FloatDoublePair pair) {
		return new FloatDoubleMutablePair(pair.getFloatKey(), pair.getDoubleValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public FloatDoublePair setFloatKey(float key);
	/**
	 * @return the Key of the Pair
	 */
	public float getFloatKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public FloatDoublePair setDoubleValue(double value);
	
	/**
	 * @return the Value of the Pair
	 */
	public double getDoubleValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public FloatDoublePair set(float key, double value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public FloatDoublePair shallowCopy();
}