package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectIntImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectIntMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectIntPair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectIntPair<?> EMPTY = new ObjectIntImmutablePair<>();
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectIntPair<T> of() {
		return (ObjectIntPair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectIntPair<T> ofKey(T key) {
		return new ObjectIntImmutablePair<>(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectIntPair<T> ofValue(int value) {
		return new ObjectIntImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectIntPair<T> of(T key, int value) {
		return new ObjectIntImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectIntPair<T> of(ObjectIntPair<T> pair) {
		return new ObjectIntImmutablePair<>(pair.getKey(), pair.getIntValue());
	}
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectIntPair<T> mutable() {
		return new ObjectIntMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectIntPair<T> mutableKey(T key) {
		return new ObjectIntMutablePair<>(key, 0);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectIntPair<T> mutableValue(int value) {
		return new ObjectIntMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectIntPair<T> mutable(T key, int value) {
		return new ObjectIntMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectIntPair<T> mutable(ObjectIntPair<T> pair) {
		return new ObjectIntMutablePair<>(pair.getKey(), pair.getIntValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectIntPair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectIntPair<T> setIntValue(int value);
	
	/**
	 * @return the Value of the Pair
	 */
	public int getIntValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ObjectIntPair<T> set(T key, int value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectIntPair<T> shallowCopy();
}