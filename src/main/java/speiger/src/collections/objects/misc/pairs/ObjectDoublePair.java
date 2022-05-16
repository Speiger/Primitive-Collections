package speiger.src.collections.objects.misc.pairs;

import speiger.src.collections.objects.misc.pairs.impl.ObjectDoubleImmutablePair;
import speiger.src.collections.objects.misc.pairs.impl.ObjectDoubleMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectDoublePair<T>
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final ObjectDoublePair<?> EMPTY = new ObjectDoubleImmutablePair<>();
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Immutable Pair
	 */
	public static <T> ObjectDoublePair<T> of() {
		return (ObjectDoublePair<T>)EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Key
	 */	
	public static <T> ObjectDoublePair<T> ofKey(T key) {
		return new ObjectDoubleImmutablePair<>(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of Value
	 */	
	public static <T> ObjectDoublePair<T> ofValue(double value) {
		return new ObjectDoubleImmutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Immutable Pair of key and value
	 */
	public static <T> ObjectDoublePair<T> of(T key, double value) {
		return new ObjectDoubleImmutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static <T> ObjectDoublePair<T> of(ObjectDoublePair<T> pair) {
		return new ObjectDoubleImmutablePair<>(pair.getKey(), pair.getDoubleValue());
	}
	
	/**
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty Mutable Pair
	 */
	public static <T> ObjectDoublePair<T> mutable() {
		return new ObjectDoubleMutablePair<>();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key
	 */
	public static <T> ObjectDoublePair<T> mutableKey(T key) {
		return new ObjectDoubleMutablePair<>(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of value
	 */
	public static <T> ObjectDoublePair<T> mutableValue(double value) {
		return new ObjectDoubleMutablePair<>(null, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @param <T> the type of elements maintained by this Collection
	 * @return Mutable Pair of key and value
	 */
	public static <T> ObjectDoublePair<T> mutable(T key, double value) {
		return new ObjectDoubleMutablePair<>(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static <T> ObjectDoublePair<T> mutable(ObjectDoublePair<T> pair) {
		return new ObjectDoubleMutablePair<>(pair.getKey(), pair.getDoubleValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public ObjectDoublePair<T> setKey(T key);
	/**
	 * @return the Key of the Pair
	 */
	public T getKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public ObjectDoublePair<T> setDoubleValue(double value);
	
	/**
	 * @return the Value of the Pair
	 */
	public double getDoubleValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public ObjectDoublePair<T> set(T key, double value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public ObjectDoublePair<T> shallowCopy();
}