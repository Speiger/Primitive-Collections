package speiger.src.collections.doubles.misc.pairs;

import speiger.src.collections.doubles.misc.pairs.impl.DoubleCharImmutablePair;
import speiger.src.collections.doubles.misc.pairs.impl.DoubleCharMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface DoubleCharPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final DoubleCharPair EMPTY = new DoubleCharImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static DoubleCharPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static DoubleCharPair ofKey(double key) {
		return new DoubleCharImmutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static DoubleCharPair ofValue(char value) {
		return new DoubleCharImmutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static DoubleCharPair of(double key, char value) {
		return new DoubleCharImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static DoubleCharPair of(DoubleCharPair pair) {
		return new DoubleCharImmutablePair(pair.getDoubleKey(), pair.getCharValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static DoubleCharPair mutable() {
		return new DoubleCharMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static DoubleCharPair mutableKey(double key) {
		return new DoubleCharMutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static DoubleCharPair mutableValue(char value) {
		return new DoubleCharMutablePair(0D, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static DoubleCharPair mutable(double key, char value) {
		return new DoubleCharMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static DoubleCharPair mutable(DoubleCharPair pair) {
		return new DoubleCharMutablePair(pair.getDoubleKey(), pair.getCharValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public DoubleCharPair setDoubleKey(double key);
	/**
	 * @return the Key of the Pair
	 */
	public double getDoubleKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public DoubleCharPair setCharValue(char value);
	
	/**
	 * @return the Value of the Pair
	 */
	public char getCharValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public DoubleCharPair set(double key, char value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public DoubleCharPair shallowCopy();
}