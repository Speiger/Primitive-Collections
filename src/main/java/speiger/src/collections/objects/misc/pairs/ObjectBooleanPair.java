package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectBooleanImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectBooleanMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectBooleanPair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectBooleanPair<?> EMPTY = new ObjectBooleanImmutablePair<>();
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectBooleanPair<T> of() {
		return (ObjectBooleanPair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectBooleanPair<T> ofKey(T key) {
		return new ObjectBooleanImmutablePair<>(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectBooleanPair<T> ofValue(boolean value) {
		return new ObjectBooleanImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectBooleanPair<T> of(T key, boolean value) {
		return new ObjectBooleanImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectBooleanPair<T> of(ObjectBooleanPair<T> pair) {
		return new ObjectBooleanImmutablePair<>(pair.getKey(), pair.getBooleanValue());
	}
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectBooleanPair<T> mutable() {
		return new ObjectBooleanMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectBooleanPair<T> mutableKey(T key) {
		return new ObjectBooleanMutablePair<>(key, false);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectBooleanPair<T> mutableValue(boolean value) {
		return new ObjectBooleanMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectBooleanPair<T> mutable(T key, boolean value) {
		return new ObjectBooleanMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectBooleanPair<T> mutable(ObjectBooleanPair<T> pair) {
		return new ObjectBooleanMutablePair<>(pair.getKey(), pair.getBooleanValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectBooleanPair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectBooleanPair<T> setBooleanValue(boolean value);
	
	/**
	 * @return the Value of the Pair
	 */
	public boolean getBooleanValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ObjectBooleanPair<T> set(T key, boolean value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectBooleanPair<T> shallowCopy();
}