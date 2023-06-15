package speiger.src.collections.shorts.misc.pairs;

import speiger.src.collections.shorts.misc.pairs.impl.ShortDoubleImmutablePair;
import speiger.src.collections.shorts.misc.pairs.impl.ShortDoubleMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ShortDoublePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ShortDoublePair EMPTY = new ShortDoubleImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ShortDoublePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ShortDoublePair ofKey(short key) {
		return new ShortDoubleImmutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ShortDoublePair ofValue(double value) {
		return new ShortDoubleImmutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ShortDoublePair of(short key, double value) {
		return new ShortDoubleImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ShortDoublePair of(ShortDoublePair pair) {
		return new ShortDoubleImmutablePair(pair.getShortKey(), pair.getDoubleValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ShortDoublePair mutable() {
		return new ShortDoubleMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ShortDoublePair mutableKey(short key) {
		return new ShortDoubleMutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ShortDoublePair mutableValue(double value) {
		return new ShortDoubleMutablePair((short)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ShortDoublePair mutable(short key, double value) {
		return new ShortDoubleMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ShortDoublePair mutable(ShortDoublePair pair) {
		return new ShortDoubleMutablePair(pair.getShortKey(), pair.getDoubleValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ShortDoublePair setShortKey(short key);
	/**
	 * @return the Key of the Pair
	 */
	public short getShortKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ShortDoublePair setDoubleValue(double value);
	
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
	public ShortDoublePair set(short key, double value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ShortDoublePair shallowCopy();
}