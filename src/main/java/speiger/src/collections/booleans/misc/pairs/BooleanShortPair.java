package speiger.src.collections.booleans.misc.pairs;

import speiger.src.collections.booleans.misc.pairs.impl.BooleanShortImmutablePair;
import speiger.src.collections.booleans.misc.pairs.impl.BooleanShortMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface BooleanShortPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final BooleanShortPair EMPTY = new BooleanShortImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static BooleanShortPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static BooleanShortPair ofKey(boolean key) {
		return new BooleanShortImmutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static BooleanShortPair ofValue(short value) {
		return new BooleanShortImmutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static BooleanShortPair of(boolean key, short value) {
		return new BooleanShortImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static BooleanShortPair of(BooleanShortPair pair) {
		return new BooleanShortImmutablePair(pair.getBooleanKey(), pair.getShortValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static BooleanShortPair mutable() {
		return new BooleanShortMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static BooleanShortPair mutableKey(boolean key) {
		return new BooleanShortMutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static BooleanShortPair mutableValue(short value) {
		return new BooleanShortMutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static BooleanShortPair mutable(boolean key, short value) {
		return new BooleanShortMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static BooleanShortPair mutable(BooleanShortPair pair) {
		return new BooleanShortMutablePair(pair.getBooleanKey(), pair.getShortValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public BooleanShortPair setBooleanKey(boolean key);
	/**
	 * @return the Key of the Pair
	 */
	public boolean getBooleanKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public BooleanShortPair setShortValue(short value);
	
	/**
	 * @return the Value of the Pair
	 */
	public short getShortValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public BooleanShortPair set(boolean key, short value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public BooleanShortPair shallowCopy();
}