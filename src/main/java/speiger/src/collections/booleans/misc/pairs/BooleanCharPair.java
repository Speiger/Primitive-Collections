package speiger.src.collections.booleans.misc.pairs;

import speiger.src.collections.booleans.misc.pairs.impl.BooleanCharImmutablePair;
import speiger.src.collections.booleans.misc.pairs.impl.BooleanCharMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface BooleanCharPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final BooleanCharPair EMPTY = new BooleanCharImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static BooleanCharPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static BooleanCharPair ofKey(boolean key) {
		return new BooleanCharImmutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static BooleanCharPair ofValue(char value) {
		return new BooleanCharImmutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static BooleanCharPair of(boolean key, char value) {
		return new BooleanCharImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static BooleanCharPair of(BooleanCharPair pair) {
		return new BooleanCharImmutablePair(pair.getBooleanKey(), pair.getCharValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static BooleanCharPair mutable() {
		return new BooleanCharMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static BooleanCharPair mutableKey(boolean key) {
		return new BooleanCharMutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static BooleanCharPair mutableValue(char value) {
		return new BooleanCharMutablePair(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static BooleanCharPair mutable(boolean key, char value) {
		return new BooleanCharMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static BooleanCharPair mutable(BooleanCharPair pair) {
		return new BooleanCharMutablePair(pair.getBooleanKey(), pair.getCharValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public BooleanCharPair setBooleanKey(boolean key);
	/**
	 * @return the Key of the Pair
	 */
	public boolean getBooleanKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public BooleanCharPair setCharValue(char value);
	
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
	public BooleanCharPair set(boolean key, char value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public BooleanCharPair shallowCopy();
}