package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectShortImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectShortMutablePair;

/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface ObjectShortPair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectShortPair<?> EMPTY = new ObjectShortImmutablePair<>();
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectShortPair<T> of() {
		return (ObjectShortPair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectShortPair<T> ofKey(T key) {
		return new ObjectShortImmutablePair<>(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectShortPair<T> ofValue(short value) {
		return new ObjectShortImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectShortPair<T> of(T key, short value) {
		return new ObjectShortImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectShortPair<T> of(ObjectShortPair<T> pair) {
		return new ObjectShortImmutablePair<>(pair.getKey(), pair.getShortValue());
	}
	
	/**
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectShortPair<T> mutable() {
		return new ObjectShortMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectShortPair<T> mutableKey(T key) {
		return new ObjectShortMutablePair<>(key, (short)0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectShortPair<T> mutableValue(short value) {
		return new ObjectShortMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectShortPair<T> mutable(T key, short value) {
		return new ObjectShortMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the keyType of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectShortPair<T> mutable(ObjectShortPair<T> pair) {
		return new ObjectShortMutablePair<>(pair.getKey(), pair.getShortValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectShortPair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectShortPair<T> setShortValue(short value);
	
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
	public ObjectShortPair<T> set(T key, short value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectShortPair<T> shallowCopy();
}