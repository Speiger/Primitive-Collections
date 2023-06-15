package speiger.src.collections.doubles.misc.pairs;

import speiger.src.collections.doubles.misc.pairs.impl.DoubleDoubleImmutablePair;
import speiger.src.collections.doubles.misc.pairs.impl.DoubleDoubleMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface DoubleDoublePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final DoubleDoublePair EMPTY = new DoubleDoubleImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static DoubleDoublePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static DoubleDoublePair ofKey(double key) {
		return new DoubleDoubleImmutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static DoubleDoublePair ofValue(double value) {
		return new DoubleDoubleImmutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static DoubleDoublePair of(double key, double value) {
		return new DoubleDoubleImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static DoubleDoublePair of(DoubleDoublePair pair) {
		return new DoubleDoubleImmutablePair(pair.getDoubleKey(), pair.getDoubleValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static DoubleDoublePair mutable() {
		return new DoubleDoubleMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static DoubleDoublePair mutableKey(double key) {
		return new DoubleDoubleMutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static DoubleDoublePair mutableValue(double value) {
		return new DoubleDoubleMutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static DoubleDoublePair mutable(double key, double value) {
		return new DoubleDoubleMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static DoubleDoublePair mutable(DoubleDoublePair pair) {
		return new DoubleDoubleMutablePair(pair.getDoubleKey(), pair.getDoubleValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public DoubleDoublePair setDoubleKey(double key);
	/**
	 * @return the Key of the Pair
	 */
	public double getDoubleKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public DoubleDoublePair setDoubleValue(double value);
	
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
	public DoubleDoublePair set(double key, double value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public DoubleDoublePair shallowCopy();
}