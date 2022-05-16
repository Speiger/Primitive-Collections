package speiger.src.collections.longs.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2ObjectMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <V> the type of elements maintained by this Collection
 * @note Long2ObjectOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Long2ObjectOrderedMap directly and will remove Long2ObjectSortedMap implementations in favor of Long2ObjectOrderedMap instead
 */
public interface Long2ObjectSortedMap<V> extends SortedMap<Long, V>, Long2ObjectMap<V>
{
	@Override
	public LongComparator comparator();
	
	@Override
	public Long2ObjectSortedMap<V> copy();
	
	@Override
	public LongSet keySet();
	@Override
	public ObjectCollection<V> values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Long2ObjectMaps#synchronize
	 */
	public default Long2ObjectSortedMap<V> synchronize() { return Long2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Long2ObjectMaps#synchronize
	 */
	public default Long2ObjectSortedMap<V> synchronize(Object mutex) { return Long2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Long2ObjectMaps#unmodifiable
	 */
	public default Long2ObjectSortedMap<V> unmodifiable() { return Long2ObjectMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Long2ObjectSortedMap<V> subMap(long fromKey, long toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Long2ObjectSortedMap<V> headMap(long toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Long2ObjectSortedMap<V> tailMap(long fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public long firstLongKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public long pollFirstLongKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public long lastLongKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public long pollLastLongKey();
	
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
	@Deprecated
	public default Long firstKey() { return Long.valueOf(firstLongKey()); }
	@Override
	@Deprecated
	public default Long lastKey() { return Long.valueOf(lastLongKey()); }
	
	@Override
	@Deprecated
	public default Long2ObjectSortedMap<V> subMap(Long fromKey, Long toKey) { return subMap(fromKey.longValue(), toKey.longValue()); }
	@Override
	@Deprecated
	public default Long2ObjectSortedMap<V> headMap(Long toKey) { return headMap(toKey.longValue()); }
	@Override
	@Deprecated
	public default Long2ObjectSortedMap<V> tailMap(Long fromKey) { return tailMap(fromKey.longValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <V> the type of elements maintained by this Collection
	 */
	interface FastSortedSet<V> extends Long2ObjectMap.FastEntrySet<V>, ObjectSortedSet<Long2ObjectMap.Entry<V>> {
		@Override
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator(long fromElement);
	}
}