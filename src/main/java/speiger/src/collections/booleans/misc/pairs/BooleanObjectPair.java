package speiger.src.collections.booleans.misc.pairs;

import speiger.src.collections.booleans.misc.pairs.impl.BooleanObjectImmutablePair;
import speiger.src.collections.booleans.misc.pairs.impl.BooleanObjectMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <V> the type of elements maintained by this Collection
 */
public interface BooleanObjectPair<V>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final BooleanObjectPair<?> EMPTY = new BooleanObjectImmutablePair<>();
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <V> BooleanObjectPair<V> of() {
		return (BooleanObjectPair<V>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <V> BooleanObjectPair<V> ofKey(boolean key) {
		return new BooleanObjectImmutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <V> BooleanObjectPair<V> ofValue(V value) {
		return new BooleanObjectImmutablePair<>(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <V> BooleanObjectPair<V> of(boolean key, V value) {
		return new BooleanObjectImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <V> BooleanObjectPair<V> of(BooleanObjectPair<V> pair) {
		return new BooleanObjectImmutablePair<>(pair.getBooleanKey(), pair.getValue());
	}
	
	/**
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <V> BooleanObjectPair<V> mutable() {
		return new BooleanObjectMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <V> BooleanObjectPair<V> mutableKey(boolean key) {
		return new BooleanObjectMutablePair<>(key, null);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <V> BooleanObjectPair<V> mutableValue(V value) {
		return new BooleanObjectMutablePair<>(false, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <V> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <V> BooleanObjectPair<V> mutable(boolean key, V value) {
		return new BooleanObjectMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <V> BooleanObjectPair<V> mutable(BooleanObjectPair<V> pair) {
		return new BooleanObjectMutablePair<>(pair.getBooleanKey(), pair.getValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public BooleanObjectPair<V> setBooleanKey(boolean key);
	/**
	 * @return the Key of the Pair
	 */
	public boolean getBooleanKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public BooleanObjectPair<V> setValue(V value);
	
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
	public BooleanObjectPair<V> set(boolean key, V value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public BooleanObjectPair<V> shallowCopy();
}