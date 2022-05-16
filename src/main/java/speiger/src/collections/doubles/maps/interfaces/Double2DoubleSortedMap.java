package speiger.src.collections.doubles.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.maps.Double2DoubleMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Double2DoubleOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Double2DoubleOrderedMap directly and will remove Double2DoubleSortedMap implementations in favor of Double2DoubleOrderedMap instead
 */
public interface Double2DoubleSortedMap extends SortedMap<Double, Double>, Double2DoubleMap
{
	@Override
	public DoubleComparator comparator();
	
	@Override
	public Double2DoubleSortedMap copy();
	
	@Override
	public DoubleSet keySet();
	@Override
	public DoubleCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Double2DoubleMaps#synchronize
	 */
	public default Double2DoubleSortedMap synchronize() { return Double2DoubleMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Double2DoubleMaps#synchronize
	 */
	public default Double2DoubleSortedMap synchronize(Object mutex) { return Double2DoubleMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Double2DoubleMaps#unmodifiable
	 */
	public default Double2DoubleSortedMap unmodifiable() { return Double2DoubleMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2DoubleSortedMap subMap(double fromKey, double toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2DoubleSortedMap headMap(double toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2DoubleSortedMap tailMap(double fromKey);
	
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
	public double firstDoubleValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public double lastDoubleValue();
	
	@Override
	@Deprecated
	public default Double firstKey() { return Double.valueOf(firstDoubleKey()); }
	@Override
	@Deprecated
	public default Double lastKey() { return Double.valueOf(lastDoubleKey()); }
	
	@Override
	@Deprecated
	public default Double2DoubleSortedMap subMap(Double fromKey, Double toKey) { return subMap(fromKey.doubleValue(), toKey.doubleValue()); }
	@Override
	@Deprecated
	public default Double2DoubleSortedMap headMap(Double toKey) { return headMap(toKey.doubleValue()); }
	@Override
	@Deprecated
	public default Double2DoubleSortedMap tailMap(Double fromKey) { return tailMap(fromKey.doubleValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Double2DoubleMap.FastEntrySet, ObjectSortedSet<Double2DoubleMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Double2DoubleMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Double2DoubleMap.Entry> fastIterator(double fromElement);
	}
}