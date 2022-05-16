package speiger.src.collections.ints.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2ByteMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Int2ByteOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Int2ByteOrderedMap directly and will remove Int2ByteSortedMap implementations in favor of Int2ByteOrderedMap instead
 */
public interface Int2ByteSortedMap extends SortedMap<Integer, Byte>, Int2ByteMap
{
	@Override
	public IntComparator comparator();
	
	@Override
	public Int2ByteSortedMap copy();
	
	@Override
	public IntSet keySet();
	@Override
	public ByteCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Int2ByteMaps#synchronize
	 */
	public default Int2ByteSortedMap synchronize() { return Int2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Int2ByteMaps#synchronize
	 */
	public default Int2ByteSortedMap synchronize(Object mutex) { return Int2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Int2ByteMaps#unmodifiable
	 */
	public default Int2ByteSortedMap unmodifiable() { return Int2ByteMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2ByteSortedMap subMap(int fromKey, int toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2ByteSortedMap headMap(int toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Int2ByteSortedMap tailMap(int fromKey);
	
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
	public byte firstByteValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public byte lastByteValue();
	
	@Override
	@Deprecated
	public default Integer firstKey() { return Integer.valueOf(firstIntKey()); }
	@Override
	@Deprecated
	public default Integer lastKey() { return Integer.valueOf(lastIntKey()); }
	
	@Override
	@Deprecated
	public default Int2ByteSortedMap subMap(Integer fromKey, Integer toKey) { return subMap(fromKey.intValue(), toKey.intValue()); }
	@Override
	@Deprecated
	public default Int2ByteSortedMap headMap(Integer toKey) { return headMap(toKey.intValue()); }
	@Override
	@Deprecated
	public default Int2ByteSortedMap tailMap(Integer fromKey) { return tailMap(fromKey.intValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Int2ByteMap.FastEntrySet, ObjectSortedSet<Int2ByteMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Int2ByteMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Int2ByteMap.Entry> fastIterator(int fromElement);
	}
}