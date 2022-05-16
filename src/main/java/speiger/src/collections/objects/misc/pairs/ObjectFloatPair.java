package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectFloatImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectFloatMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectFloatPair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectFloatPair<?> EMPTY = new ObjectFloatImmutablePair<>();
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectFloatPair<T> of() {
		return (ObjectFloatPair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectFloatPair<T> ofKey(T key) {
		return new ObjectFloatImmutablePair<>(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectFloatPair<T> ofValue(float value) {
		return new ObjectFloatImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectFloatPair<T> of(T key, float value) {
		return new ObjectFloatImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectFloatPair<T> of(ObjectFloatPair<T> pair) {
		return new ObjectFloatImmutablePair<>(pair.getKey(), pair.getFloatValue());
	}
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectFloatPair<T> mutable() {
		return new ObjectFloatMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectFloatPair<T> mutableKey(T key) {
		return new ObjectFloatMutablePair<>(key, 0F);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectFloatPair<T> mutableValue(float value) {
		return new ObjectFloatMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectFloatPair<T> mutable(T key, float value) {
		return new ObjectFloatMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectFloatPair<T> mutable(ObjectFloatPair<T> pair) {
		return new ObjectFloatMutablePair<>(pair.getKey(), pair.getFloatValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectFloatPair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectFloatPair<T> setFloatValue(float value);
	
	/**
	 * @return the Value of the Pair
	 */
	public float getFloatValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ObjectFloatPair<T> set(T key, float value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectFloatPair<T> shallowCopy();
}