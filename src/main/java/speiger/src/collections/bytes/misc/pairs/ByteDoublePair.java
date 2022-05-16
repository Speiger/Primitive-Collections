package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteDoubleImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteDoubleMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteDoublePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteDoublePair EMPTY = new ByteDoubleImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteDoublePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteDoublePair ofKey(byte key) {
		return new ByteDoubleImmutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteDoublePair ofValue(double value) {
		return new ByteDoubleImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteDoublePair of(byte key, double value) {
		return new ByteDoubleImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteDoublePair of(ByteDoublePair pair) {
		return new ByteDoubleImmutablePair(pair.getByteKey(), pair.getDoubleValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteDoublePair mutable() {
		return new ByteDoubleMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteDoublePair mutableKey(byte key) {
		return new ByteDoubleMutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteDoublePair mutableValue(double value) {
		return new ByteDoubleMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteDoublePair mutable(byte key, double value) {
		return new ByteDoubleMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteDoublePair mutable(ByteDoublePair pair) {
		return new ByteDoubleMutablePair(pair.getByteKey(), pair.getDoubleValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteDoublePair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteDoublePair setDoubleValue(double value);
	
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
	public ByteDoublePair set(byte key, double value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteDoublePair shallowCopy();
}