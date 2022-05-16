package speiger.src.collections.objects.maps.interfaces;

import java.util.Comparator;
import java.util.SortedMap;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2DoubleMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <T> the type of elements maintained by this Collection
 * @note Object2DoubleOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Object2DoubleOrderedMap directly and will remove Object2DoubleSortedMap implementations in favor of Object2DoubleOrderedMap instead
 */
public interface Object2DoubleSortedMap<T> extends SortedMap<T, Double>, Object2DoubleMap<T>
{
	@Override
	public Comparator<T> comparator();
	
	@Override
	public Object2DoubleSortedMap<T> copy();
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public DoubleCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Object2DoubleMaps#synchronize
	 */
	public default Object2DoubleSortedMap<T> synchronize() { return Object2DoubleMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Object2DoubleMaps#synchronize
	 */
	public default Object2DoubleSortedMap<T> synchronize(Object mutex) { return Object2DoubleMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Object2DoubleMaps#unmodifiable
	 */
	public default Object2DoubleSortedMap<T> unmodifiable() { return Object2DoubleMaps.unmodifiable(this); }
	
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
	public double firstDoubleValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public double lastDoubleValue();
	
	@Override
	public default Object2DoubleSortedMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, toKey); }
	@Override
	public default Object2DoubleSortedMap<T> headMap(T toKey) { return headMap(toKey); }
	@Override
	public default Object2DoubleSortedMap<T> tailMap(T fromKey) { return tailMap(fromKey); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	interface FastSortedSet<T> extends Object2DoubleMap.FastEntrySet<T>, ObjectSortedSet<Object2DoubleMap.Entry<T>> {
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> fastIterator(T fromElement);
	}
}