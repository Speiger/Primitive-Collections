package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectObjectImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectObjectMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the keyType of elements maintained by this Collection
 * @param <V> the keyType of elements maintained by this Collection
 */
public interface ObjectObjectPair<T, V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectObjectPair<?, ?> EMPTY = new ObjectObjectImmutablePair<>();
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T, V> ObjectObjectPair<T, V> of() {
		return (ObjectObjectPair<T, V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T, V> ObjectObjectPair<T, V> ofKey(T key) {
		return new ObjectObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T, V> ObjectObjectPair<T, V> ofValue(V value) {
		return new ObjectObjectImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T, V> ObjectObjectPair<T, V> of(T key, V value) {
		return new ObjectObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T, V> ObjectObjectPair<T, V> of(ObjectObjectPair<T, V> pair) {
		return new ObjectObjectImmutablePair<>(pair.getKey(), pair.getValue());
	}
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T, V> ObjectObjectPair<T, V> mutable() {
		return new ObjectObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T, V> ObjectObjectPair<T, V> mutableKey(T key) {
		return new ObjectObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T, V> ObjectObjectPair<T, V> mutableValue(V value) {
		return new ObjectObjectMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T, V> ObjectObjectPair<T, V> mutable(T key, V value) {
		return new ObjectObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T, V> ObjectObjectPair<T, V> mutable(ObjectObjectPair<T, V> pair) {
		return new ObjectObjectMutablePair<>(pair.getKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectObjectPair<T, V> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectObjectPair<T, V> setValue(V value);
	
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
	public ObjectObjectPair<T, V> set(T key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectObjectPair<T, V> shallowCopy();
}