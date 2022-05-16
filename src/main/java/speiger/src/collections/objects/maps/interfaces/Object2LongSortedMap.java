package speiger.src.collections.objects.maps.interfaces;

import java.util.Comparator;
import java.util.SortedMap;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2LongMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <T> the type of elements maintained by this Collection
 * @note Object2LongOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Object2LongOrderedMap directly and will remove Object2LongSortedMap implementations in favor of Object2LongOrderedMap instead
 */
public interface Object2LongSortedMap<T> extends SortedMap<T, Long>, Object2LongMap<T>
{
	@Override
	public Comparator<T> comparator();
	
	@Override
	public Object2LongSortedMap<T> copy();
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public LongCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	public default Object2LongSortedMap<T> synchronize() { return Object2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	public default Object2LongSortedMap<T> synchronize(Object mutex) { return Object2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Object2LongMaps#unmodifiable
	 */
	public default Object2LongSortedMap<T> unmodifiable() { return Object2LongMaps.unmodifiable(this); }
	
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
	public long firstLongValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public long lastLongValue();
	
	@Override
	public default Object2LongSortedMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, toKey); }
	@Override
	public default Object2LongSortedMap<T> headMap(T toKey) { return headMap(toKey); }
	@Override
	public default Object2LongSortedMap<T> tailMap(T fromKey) { return tailMap(fromKey); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	interface FastSortedSet<T> extends Object2LongMap.FastEntrySet<T>, ObjectSortedSet<Object2LongMap.Entry<T>> {
		@Override
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> fastIterator(T fromElement);
	}
}