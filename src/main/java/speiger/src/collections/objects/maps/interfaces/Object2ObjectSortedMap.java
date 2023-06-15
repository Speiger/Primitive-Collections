package speiger.src.collections.objects.maps.interfaces;

import java.util.Comparator;
import java.util.SortedMap;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.utils.maps.Object2ObjectMaps;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <T> the keyType of elements maintained by this Collection
 * @param <V> the keyType of elements maintained by this Collection
 * @note Object2ObjectOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Object2ObjectOrderedMap directly and will remove Object2ObjectSortedMap implementations in favor of Object2ObjectOrderedMap instead
 */
public interface Object2ObjectSortedMap<T, V> extends SortedMap<T, V>, Object2ObjectMap<T, V>
{
	@Override
	public Comparator<T> comparator();
	
	@Override
	public Object2ObjectSortedMap<T, V> copy();
	
	@Override
	public ObjectSortedSet<T> keySet();
	@Override
	public ObjectCollection<V> values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Object2ObjectMaps#synchronize
	 */
	public default Object2ObjectSortedMap<T, V> synchronize() { return Object2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Object2ObjectMaps#synchronize
	 */
	public default Object2ObjectSortedMap<T, V> synchronize(Object mutex) { return Object2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Object2ObjectMaps#unmodifiable
	 */
	public default Object2ObjectSortedMap<T, V> unmodifiable() { return Object2ObjectMaps.unmodifiable(this); }
	
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public T pollFirstKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public T pollLastKey();
	/**
	 * A method to get the first Value of a Map.
	 * @return the first key in the map
	 */
	public V firstValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public V lastValue();
	
	@Override
	public Object2ObjectSortedMap<T, V> subMap(T fromKey, T toKey);
	@Override
	public Object2ObjectSortedMap<T, V> headMap(T toKey);
	@Override
	public Object2ObjectSortedMap<T, V> tailMap(T fromKey);
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the keyType of elements maintained by this Collection
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	interface FastSortedSet<T, V> extends Object2ObjectMap.FastEntrySet<T, V>, ObjectSortedSet<Object2ObjectMap.Entry<T, V>> {
		@Override
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Object2ObjectMap.Entry<T, V>> fastIterator(T fromElement);
	}
}