package speiger.src.collections.doubles.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.utils.maps.Double2IntMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Double2IntOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Double2IntOrderedMap directly and will remove Double2IntSortedMap implementations in favor of Double2IntOrderedMap instead
 */
public interface Double2IntSortedMap extends SortedMap<Double, Integer>, Double2IntMap
{
	@Override
	public DoubleComparator comparator();
	
	@Override
	public Double2IntSortedMap copy();
	
	@Override
	public DoubleSortedSet keySet();
	@Override
	public IntCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Double2IntMaps#synchronize
	 */
	public default Double2IntSortedMap synchronize() { return Double2IntMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Double2IntMaps#synchronize
	 */
	public default Double2IntSortedMap synchronize(Object mutex) { return Double2IntMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Double2IntMaps#unmodifiable
	 */
	public default Double2IntSortedMap unmodifiable() { return Double2IntMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2IntSortedMap subMap(double fromKey, double toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2IntSortedMap headMap(double toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2IntSortedMap tailMap(double fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public double firstDoubleKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public double pollFirstDoubleKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public double lastDoubleKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public double pollLastDoubleKey();
	
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
	public default Double firstKey() { return Double.valueOf(firstDoubleKey()); }
	@Override
	@Deprecated
	public default Double lastKey() { return Double.valueOf(lastDoubleKey()); }
	
	@Override
	@Deprecated
	public default Double2IntSortedMap subMap(Double fromKey, Double toKey) { return subMap(fromKey.doubleValue(), toKey.doubleValue()); }
	@Override
	@Deprecated
	public default Double2IntSortedMap headMap(Double toKey) { return headMap(toKey.doubleValue()); }
	@Override
	@Deprecated
	public default Double2IntSortedMap tailMap(Double fromKey) { return tailMap(fromKey.doubleValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Double2IntMap.FastEntrySet, ObjectSortedSet<Double2IntMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Double2IntMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Double2IntMap.Entry> fastIterator(double fromElement);
	}
}