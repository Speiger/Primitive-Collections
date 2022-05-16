package speiger.src.collections.booleans.misc.pairs;

import speiger.src.collections.booleans.misc.pairs.impl.BooleanIntImmutablePair;
import speiger.src.collections.booleans.misc.pairs.impl.BooleanIntMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface BooleanIntPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final BooleanIntPair EMPTY = new BooleanIntImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static BooleanIntPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static BooleanIntPair ofKey(boolean key) {
		return new BooleanIntImmutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static BooleanIntPair ofValue(int value) {
		return new BooleanIntImmutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static BooleanIntPair of(boolean key, int value) {
		return new BooleanIntImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static BooleanIntPair of(BooleanIntPair pair) {
		return new BooleanIntImmutablePair(pair.getBooleanKey(), pair.getIntValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static BooleanIntPair mutable() {
		return new BooleanIntMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static BooleanIntPair mutableKey(boolean key) {
		return new BooleanIntMutablePair(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static BooleanIntPair mutableValue(int value) {
		return new BooleanIntMutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static BooleanIntPair mutable(boolean key, int value) {
		return new BooleanIntMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static BooleanIntPair mutable(BooleanIntPair pair) {
		return new BooleanIntMutablePair(pair.getBooleanKey(), pair.getIntValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public BooleanIntPair setBooleanKey(boolean key);
	/**
	 * @return the Key of the Pair
	 */
	public boolean getBooleanKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public BooleanIntPair setIntValue(int value);
	
	/**
	 * @return the Value of the Pair
	 */
	public int getIntValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public BooleanIntPair set(boolean key, int value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public BooleanIntPair shallowCopy();
}