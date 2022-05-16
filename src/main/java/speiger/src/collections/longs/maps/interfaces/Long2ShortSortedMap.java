package speiger.src.collections.longs.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2ShortMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Long2ShortOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Long2ShortOrderedMap directly and will remove Long2ShortSortedMap implementations in favor of Long2ShortOrderedMap instead
 */
public interface Long2ShortSortedMap extends SortedMap<Long, Short>, Long2ShortMap
{
	@Override
	public LongComparator comparator();
	
	@Override
	public Long2ShortSortedMap copy();
	
	@Override
	public LongSet keySet();
	@Override
	public ShortCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Long2ShortMaps#synchronize
	 */
	public default Long2ShortSortedMap synchronize() { return Long2ShortMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Long2ShortMaps#synchronize
	 */
	public default Long2ShortSortedMap synchronize(Object mutex) { return Long2ShortMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Long2ShortMaps#unmodifiable
	 */
	public default Long2ShortSortedMap unmodifiable() { return Long2ShortMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Long2ShortSortedMap subMap(long fromKey, long toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Long2ShortSortedMap headMap(long toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Long2ShortSortedMap tailMap(long fromKey);
	
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
	public short firstShortValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public short lastShortValue();
	
	@Override
	@Deprecated
	public default Long firstKey() { return Long.valueOf(firstLongKey()); }
	@Override
	@Deprecated
	public default Long lastKey() { return Long.valueOf(lastLongKey()); }
	
	@Override
	@Deprecated
	public default Long2ShortSortedMap subMap(Long fromKey, Long toKey) { return subMap(fromKey.longValue(), toKey.longValue()); }
	@Override
	@Deprecated
	public default Long2ShortSortedMap headMap(Long toKey) { return headMap(toKey.longValue()); }
	@Override
	@Deprecated
	public default Long2ShortSortedMap tailMap(Long fromKey) { return tailMap(fromKey.longValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Long2ShortMap.FastEntrySet, ObjectSortedSet<Long2ShortMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Long2ShortMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Long2ShortMap.Entry> fastIterator(long fromElement);
	}
}