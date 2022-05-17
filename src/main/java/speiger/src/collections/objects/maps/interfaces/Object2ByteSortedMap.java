package speiger.src.collections.objects.maps.interfaces;

import java.util.Comparator;
import java.util.SortedMap;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2ByteMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <T> the type of elements maintained by this Collection
 * @note Object2ByteOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Object2ByteOrderedMap directly and will remove Object2ByteSortedMap implementations in favor of Object2ByteOrderedMap instead
 */
public interface Object2ByteSortedMap<T> extends SortedMap<T, Byte>, Object2ByteMap<T>
{
	@Override
	public Comparator<T> comparator();
	
	@Override
	public Object2ByteSortedMap<T> copy();
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public ByteCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Object2ByteMaps#synchronize
	 */
	public default Object2ByteSortedMap<T> synchronize() { return Object2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Object2ByteMaps#synchronize
	 */
	public default Object2ByteSortedMap<T> synchronize(Object mutex) { return Object2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Object2ByteMaps#unmodifiable
	 */
	public default Object2ByteSortedMap<T> unmodifiable() { return Object2ByteMaps.unmodifiable(this); }
	
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
	public byte firstByteValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public byte lastByteValue();
	
	@Override
	public Object2ByteSortedMap<T> subMap(T fromKey, T toKey);
	@Override
	public Object2ByteSortedMap<T> headMap(T toKey);
	@Override
	public Object2ByteSortedMap<T> tailMap(T fromKey);
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	interface FastSortedSet<T> extends Object2ByteMap.FastEntrySet<T>, ObjectSortedSet<Object2ByteMap.Entry<T>> {
		@Override
		public ObjectBidirectionalIterator<Object2ByteMap.Entry<T>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Object2ByteMap.Entry<T>> fastIterator(T fromElement);
	}
}