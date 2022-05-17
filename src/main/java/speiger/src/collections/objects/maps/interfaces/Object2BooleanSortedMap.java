package speiger.src.collections.objects.maps.interfaces;

import java.util.Comparator;
import java.util.SortedMap;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2BooleanMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <T> the type of elements maintained by this Collection
 * @note Object2BooleanOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Object2BooleanOrderedMap directly and will remove Object2BooleanSortedMap implementations in favor of Object2BooleanOrderedMap instead
 */
public interface Object2BooleanSortedMap<T> extends SortedMap<T, Boolean>, Object2BooleanMap<T>
{
	@Override
	public Comparator<T> comparator();
	
	@Override
	public Object2BooleanSortedMap<T> copy();
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public BooleanCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Object2BooleanMaps#synchronize
	 */
	public default Object2BooleanSortedMap<T> synchronize() { return Object2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Object2BooleanMaps#synchronize
	 */
	public default Object2BooleanSortedMap<T> synchronize(Object mutex) { return Object2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Object2BooleanMaps#unmodifiable
	 */
	public default Object2BooleanSortedMap<T> unmodifiable() { return Object2BooleanMaps.unmodifiable(this); }
	
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
	public boolean firstBooleanValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public boolean lastBooleanValue();
	
	@Override
	public Object2BooleanSortedMap<T> subMap(T fromKey, T toKey);
	@Override
	public Object2BooleanSortedMap<T> headMap(T toKey);
	@Override
	public Object2BooleanSortedMap<T> tailMap(T fromKey);
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	interface FastSortedSet<T> extends Object2BooleanMap.FastEntrySet<T>, ObjectSortedSet<Object2BooleanMap.Entry<T>> {
		@Override
		public ObjectBidirectionalIterator<Object2BooleanMap.Entry<T>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Object2BooleanMap.Entry<T>> fastIterator(T fromElement);
	}
}