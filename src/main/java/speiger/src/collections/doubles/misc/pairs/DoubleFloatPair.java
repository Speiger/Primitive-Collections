package speiger.src.collections.doubles.misc.pairs;

import speiger.src.collections.doubles.misc.pairs.impl.DoubleFloatImmutablePair;
import speiger.src.collections.doubles.misc.pairs.impl.DoubleFloatMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface DoubleFloatPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final DoubleFloatPair EMPTY = new DoubleFloatImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static DoubleFloatPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static DoubleFloatPair ofKey(double key) {
		return new DoubleFloatImmutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static DoubleFloatPair ofValue(float value) {
		return new DoubleFloatImmutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static DoubleFloatPair of(double key, float value) {
		return new DoubleFloatImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static DoubleFloatPair of(DoubleFloatPair pair) {
		return new DoubleFloatImmutablePair(pair.getDoubleKey(), pair.getFloatValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static DoubleFloatPair mutable() {
		return new DoubleFloatMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static DoubleFloatPair mutableKey(double key) {
		return new DoubleFloatMutablePair(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static DoubleFloatPair mutableValue(float value) {
		return new DoubleFloatMutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static DoubleFloatPair mutable(double key, float value) {
		return new DoubleFloatMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static DoubleFloatPair mutable(DoubleFloatPair pair) {
		return new DoubleFloatMutablePair(pair.getDoubleKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public DoubleFloatPair setDoubleKey(double key);
	/**
	 * @return the Key of the Pair
	 */
	public double getDoubleKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public DoubleFloatPair setFloatValue(float value);
	
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
	public DoubleFloatPair set(double key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public DoubleFloatPair shallowCopy();
}