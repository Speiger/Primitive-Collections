package speiger.src.collections.floats.misc.pairs;

import speiger.src.collections.floats.misc.pairs.impl.FloatObjectImmutablePair;
import speiger.src.collections.floats.misc.pairs.impl.FloatObjectMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the type of elements maintained by this Collection
 */
public interface FloatObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final FloatObjectPair<?> EMPTY = new FloatObjectImmutablePair<>();
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> FloatObjectPair<V> of() {
		return (FloatObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> FloatObjectPair<V> ofKey(float key) {
		return new FloatObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> FloatObjectPair<V> ofValue(V value) {
		return new FloatObjectImmutablePair<>(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> FloatObjectPair<V> of(float key, V value) {
		return new FloatObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> FloatObjectPair<V> of(FloatObjectPair<V> pair) {
		return new FloatObjectImmutablePair<>(pair.getFloatKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> FloatObjectPair<V> mutable() {
		return new FloatObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> FloatObjectPair<V> mutableKey(float key) {
		return new FloatObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> FloatObjectPair<V> mutableValue(V value) {
		return new FloatObjectMutablePair<>(0F, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> FloatObjectPair<V> mutable(float key, V value) {
		return new FloatObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> FloatObjectPair<V> mutable(FloatObjectPair<V> pair) {
		return new FloatObjectMutablePair<>(pair.getFloatKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public FloatObjectPair<V> setFloatKey(float key);
	/**
	 * @return the Key of the Pair
	 */
	public float getFloatKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public FloatObjectPair<V> setValue(V value);
	
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
	public FloatObjectPair<V> set(float key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public FloatObjectPair<V> shallowCopy();
}