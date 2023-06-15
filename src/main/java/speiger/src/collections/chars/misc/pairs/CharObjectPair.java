package speiger.src.collections.chars.misc.pairs;

import speiger.src.collections.chars.misc.pairs.impl.CharObjectImmutablePair;
import speiger.src.collections.chars.misc.pairs.impl.CharObjectMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface CharObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final CharObjectPair<?> EMPTY = new CharObjectImmutablePair<>();
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> CharObjectPair<V> of() {
		return (CharObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> CharObjectPair<V> ofKey(char key) {
		return new CharObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> CharObjectPair<V> ofValue(V value) {
		return new CharObjectImmutablePair<>((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> CharObjectPair<V> of(char key, V value) {
		return new CharObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> CharObjectPair<V> of(CharObjectPair<V> pair) {
		return new CharObjectImmutablePair<>(pair.getCharKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> CharObjectPair<V> mutable() {
		return new CharObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> CharObjectPair<V> mutableKey(char key) {
		return new CharObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> CharObjectPair<V> mutableValue(V value) {
		return new CharObjectMutablePair<>((char)0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> CharObjectPair<V> mutable(char key, V value) {
		return new CharObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> CharObjectPair<V> mutable(CharObjectPair<V> pair) {
		return new CharObjectMutablePair<>(pair.getCharKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public CharObjectPair<V> setCharKey(char key);
	/**
	 * @return the Key of the Pair
	 */
	public char getCharKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public CharObjectPair<V> setValue(V value);
	
	/**
	 * @return the Value of the Pair
	 */
	public V getValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public CharObjectPair<V> set(char key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public CharObjectPair<V> shallowCopy();
}