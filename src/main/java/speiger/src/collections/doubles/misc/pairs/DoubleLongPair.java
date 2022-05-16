package speiger.src.collections.doubles.misc.pairs;

import speiger.src.collections.doubles.misc.pairs.impl.DoubleLongImmutablePair;
import speiger.src.collections.doubles.misc.pairs.impl.DoubleLongMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface DoubleLongPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final DoubleLongPair EMPTY = new DoubleLongImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static DoubleLongPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static DoubleLongPair ofKey(double key) {
		return new DoubleLongImmutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static DoubleLongPair ofValue(long value) {
		return new DoubleLongImmutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static DoubleLongPair of(double key, long value) {
		return new DoubleLongImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static DoubleLongPair of(DoubleLongPair pair) {
		return new DoubleLongImmutablePair(pair.getDoubleKey(), pair.getLongValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static DoubleLongPair mutable() {
		return new DoubleLongMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static DoubleLongPair mutableKey(double key) {
		return new DoubleLongMutablePair(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static DoubleLongPair mutableValue(long value) {
		return new DoubleLongMutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static DoubleLongPair mutable(double key, long value) {
		return new DoubleLongMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static DoubleLongPair mutable(DoubleLongPair pair) {
		return new DoubleLongMutablePair(pair.getDoubleKey(), pair.getLongValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public DoubleLongPair setDoubleKey(double key);
	/**
	 * @return the Key of the Pair
	 */
	public double getDoubleKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public DoubleLongPair setLongValue(long value);
	
	/**
	 * @return the Value of the Pair
	 */
	public long getLongValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public DoubleLongPair set(double key, long value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public DoubleLongPair shallowCopy();
}