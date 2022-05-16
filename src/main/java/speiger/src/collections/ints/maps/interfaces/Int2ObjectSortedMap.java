package speiger.src.collections.ints.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2ObjectMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <V> the type of elements maintained by this Collection
 * @note Int2ObjectOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Int2ObjectOrderedMap directly and will remove Int2ObjectSortedMap implementations in favor of Int2ObjectOrderedMap instead
 */
public interface Int2ObjectSortedMap<V> extends SortedMap<Integer, V>, Int2ObjectMap<V>
{
	@Override
	public IntComparator comparator();
	
	@Override
	public Int2ObjectSortedMap<V> copy();
	
	@Override
	public IntSet keySet();
	@Override
	public ObjectCollection<V> values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Int2ObjectMaps#synchronize
	 */
	public default Int2ObjectSortedMap<V> synchronize() { return Int2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Int2ObjectMaps#synchronize
	 */
	public default Int2ObjectSortedMap<V> synchronize(Object mutex) { return Int2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Int2ObjectMaps#unmodifiable
	 */
	public default Int2ObjectSortedMap<V> unmodifiable() { return Int2ObjectMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2ObjectSortedMap<V> subMap(int fromKey, int toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2ObjectSortedMap<V> headMap(int toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2ObjectSortedMap<V> tailMap(int fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public int firstIntKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public int pollFirstIntKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public int lastIntKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public int pollLastIntKey();
	
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
	public default Integer firstKey() { return Integer.valueOf(firstIntKey()); }
	@Override
	@Deprecated
	public default Integer lastKey() { return Integer.valueOf(lastIntKey()); }
	
	@Override
	@Deprecated
	public default Int2ObjectSortedMap<V> subMap(Integer fromKey, Integer toKey) { return subMap(fromKey.intValue(), toKey.intValue()); }
	@Override
	@Deprecated
	public default Int2ObjectSortedMap<V> headMap(Integer toKey) { return headMap(toKey.intValue()); }
	@Override
	@Deprecated
	public default Int2ObjectSortedMap<V> tailMap(Integer fromKey) { return tailMap(fromKey.intValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <V> the type of elements maintained by this Collection
	 */
	interface FastSortedSet<V> extends Int2ObjectMap.FastEntrySet<V>, ObjectSortedSet<Int2ObjectMap.Entry<V>> {
		@Override
		public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator(int fromElement);
	}
}