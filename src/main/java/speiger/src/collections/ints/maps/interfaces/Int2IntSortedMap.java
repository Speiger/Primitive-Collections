package speiger.src.collections.ints.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2IntMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Int2IntOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Int2IntOrderedMap directly and will remove Int2IntSortedMap implementations in favor of Int2IntOrderedMap instead
 */
public interface Int2IntSortedMap extends SortedMap<Integer, Integer>, Int2IntMap
{
	@Override
	public IntComparator comparator();
	
	@Override
	public Int2IntSortedMap copy();
	
	@Override
	public IntSet keySet();
	@Override
	public IntCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Int2IntMaps#synchronize
	 */
	public default Int2IntSortedMap synchronize() { return Int2IntMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Int2IntMaps#synchronize
	 */
	public default Int2IntSortedMap synchronize(Object mutex) { return Int2IntMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Int2IntMaps#unmodifiable
	 */
	public default Int2IntSortedMap unmodifiable() { return Int2IntMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2IntSortedMap subMap(int fromKey, int toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2IntSortedMap headMap(int toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2IntSortedMap tailMap(int fromKey);
	
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
	public int firstIntValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public int lastIntValue();
	
	@Override
	@Deprecated
	public default Integer firstKey() { return Integer.valueOf(firstIntKey()); }
	@Override
	@Deprecated
	public default Integer lastKey() { return Integer.valueOf(lastIntKey()); }
	
	@Override
	@Deprecated
	public default Int2IntSortedMap subMap(Integer fromKey, Integer toKey) { return subMap(fromKey.intValue(), toKey.intValue()); }
	@Override
	@Deprecated
	public default Int2IntSortedMap headMap(Integer toKey) { return headMap(toKey.intValue()); }
	@Override
	@Deprecated
	public default Int2IntSortedMap tailMap(Integer fromKey) { return tailMap(fromKey.intValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Int2IntMap.FastEntrySet, ObjectSortedSet<Int2IntMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Int2IntMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Int2IntMap.Entry> fastIterator(int fromElement);
	}
}