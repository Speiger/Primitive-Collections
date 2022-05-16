package speiger.src.collections.doubles.misc.pairs;

import speiger.src.collections.doubles.misc.pairs.impl.DoubleBooleanImmutablePair;
import speiger.src.collections.doubles.misc.pairs.impl.DoubleBooleanMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface DoubleBooleanPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final DoubleBooleanPair EMPTY = new DoubleBooleanImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static DoubleBooleanPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static DoubleBooleanPair ofKey(double key) {
		return new DoubleBooleanImmutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static DoubleBooleanPair ofValue(boolean value) {
		return new DoubleBooleanImmutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static DoubleBooleanPair of(double key, boolean value) {
		return new DoubleBooleanImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static DoubleBooleanPair of(DoubleBooleanPair pair) {
		return new DoubleBooleanImmutablePair(pair.getDoubleKey(), pair.getBooleanValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static DoubleBooleanPair mutable() {
		return new DoubleBooleanMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static DoubleBooleanPair mutableKey(double key) {
		return new DoubleBooleanMutablePair(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static DoubleBooleanPair mutableValue(boolean value) {
		return new DoubleBooleanMutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static DoubleBooleanPair mutable(double key, boolean value) {
		return new DoubleBooleanMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static DoubleBooleanPair mutable(DoubleBooleanPair pair) {
		return new DoubleBooleanMutablePair(pair.getDoubleKey(), pair.getBooleanValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public DoubleBooleanPair setDoubleKey(double key);
	/**
	 * @return the Key of the Pair
	 */
	public double getDoubleKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public DoubleBooleanPair setBooleanValue(boolean value);
	
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
	public DoubleBooleanPair set(double key, boolean value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public DoubleBooleanPair shallowCopy();
}