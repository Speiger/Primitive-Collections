package speiger.src.collections.floats.misc.pairs;

import speiger.src.collections.floats.misc.pairs.impl.FloatShortImmutablePair;
import speiger.src.collections.floats.misc.pairs.impl.FloatShortMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface FloatShortPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final FloatShortPair EMPTY = new FloatShortImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static FloatShortPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static FloatShortPair ofKey(float key) {
		return new FloatShortImmutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static FloatShortPair ofValue(short value) {
		return new FloatShortImmutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static FloatShortPair of(float key, short value) {
		return new FloatShortImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static FloatShortPair of(FloatShortPair pair) {
		return new FloatShortImmutablePair(pair.getFloatKey(), pair.getShortValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static FloatShortPair mutable() {
		return new FloatShortMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static FloatShortPair mutableKey(float key) {
		return new FloatShortMutablePair(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static FloatShortPair mutableValue(short value) {
		return new FloatShortMutablePair(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static FloatShortPair mutable(float key, short value) {
		return new FloatShortMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static FloatShortPair mutable(FloatShortPair pair) {
		return new FloatShortMutablePair(pair.getFloatKey(), pair.getShortValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public FloatShortPair setFloatKey(float key);
	/**
	 * @return the Key of the Pair
	 */
	public float getFloatKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public FloatShortPair setShortValue(short value);
	
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
	public FloatShortPair set(float key, short value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public FloatShortPair shallowCopy();
}