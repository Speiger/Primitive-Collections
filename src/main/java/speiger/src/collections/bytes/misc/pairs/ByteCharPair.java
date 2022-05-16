package speiger.src.collections.bytes.misc.pairs;

import speiger.src.collections.bytes.misc.pairs.impl.ByteCharImmutablePair;
import speiger.src.collections.bytes.misc.pairs.impl.ByteCharMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface ByteCharPair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ByteCharPair EMPTY = new ByteCharImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static ByteCharPair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static ByteCharPair ofKey(byte key) {
		return new ByteCharImmutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static ByteCharPair ofValue(char value) {
		return new ByteCharImmutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static ByteCharPair of(byte key, char value) {
		return new ByteCharImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static ByteCharPair of(ByteCharPair pair) {
		return new ByteCharImmutablePair(pair.getByteKey(), pair.getCharValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static ByteCharPair mutable() {
		return new ByteCharMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static ByteCharPair mutableKey(byte key) {
		return new ByteCharMutablePair(key, (char)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static ByteCharPair mutableValue(char value) {
		return new ByteCharMutablePair((byte)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static ByteCharPair mutable(byte key, char value) {
		return new ByteCharMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static ByteCharPair mutable(ByteCharPair pair) {
		return new ByteCharMutablePair(pair.getByteKey(), pair.getCharValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ByteCharPair setByteKey(byte key);
	/**
	 * @return the Key of the Pair
	 */
	public byte getByteKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ByteCharPair setCharValue(char value);
	
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
	public ByteCharPair set(byte key, char value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ByteCharPair shallowCopy();
}