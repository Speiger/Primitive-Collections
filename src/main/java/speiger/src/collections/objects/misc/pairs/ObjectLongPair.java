package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectLongImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectLongMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectLongPair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectLongPair<?> EMPTY = new ObjectLongImmutablePair<>();
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectLongPair<T> of() {
		return (ObjectLongPair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectLongPair<T> ofKey(T key) {
		return new ObjectLongImmutablePair<>(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectLongPair<T> ofValue(long value) {
		return new ObjectLongImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectLongPair<T> of(T key, long value) {
		return new ObjectLongImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectLongPair<T> of(ObjectLongPair<T> pair) {
		return new ObjectLongImmutablePair<>(pair.getKey(), pair.getLongValue());
	}
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectLongPair<T> mutable() {
		return new ObjectLongMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectLongPair<T> mutableKey(T key) {
		return new ObjectLongMutablePair<>(key, 0L);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectLongPair<T> mutableValue(long value) {
		return new ObjectLongMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectLongPair<T> mutable(T key, long value) {
		return new ObjectLongMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectLongPair<T> mutable(ObjectLongPair<T> pair) {
		return new ObjectLongMutablePair<>(pair.getKey(), pair.getLongValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectLongPair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectLongPair<T> setLongValue(long value);
	
	/**
	 * @return the Value of the Pair
	 */
	public long getLongValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ObjectLongPair<T> set(T key, long value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectLongPair<T> shallowCopy();
}