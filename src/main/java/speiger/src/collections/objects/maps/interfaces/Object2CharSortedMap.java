package speiger.src.collections.objects.maps.interfaces;

import java.util.Comparator;
import java.util.SortedMap;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2CharMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <T> the type of elements maintained by this Collection
 * @note Object2CharOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Object2CharOrderedMap directly and will remove Object2CharSortedMap implementations in favor of Object2CharOrderedMap instead
 */
public interface Object2CharSortedMap<T> extends SortedMap<T, Character>, Object2CharMap<T>
{
	@Override
	public Comparator<T> comparator();
	
	@Override
	public Object2CharSortedMap<T> copy();
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public CharCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Object2CharMaps#synchronize
	 */
	public default Object2CharSortedMap<T> synchronize() { return Object2CharMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Object2CharMaps#synchronize
	 */
	public default Object2CharSortedMap<T> synchronize(Object mutex) { return Object2CharMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Object2CharMaps#unmodifiable
	 */
	public default Object2CharSortedMap<T> unmodifiable() { return Object2CharMaps.unmodifiable(this); }
	
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
	public char firstCharValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public char lastCharValue();
	
	@Override
	public Object2CharSortedMap<T> subMap(T fromKey, T toKey);
	@Override
	public Object2CharSortedMap<T> headMap(T toKey);
	@Override
	public Object2CharSortedMap<T> tailMap(T fromKey);
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	interface FastSortedSet<T> extends Object2CharMap.FastEntrySet<T>, ObjectSortedSet<Object2CharMap.Entry<T>> {
		@Override
		public ObjectBidirectionalIterator<Object2CharMap.Entry<T>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Object2CharMap.Entry<T>> fastIterator(T fromElement);
	}
}