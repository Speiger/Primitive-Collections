package speiger.src.collections.bytes.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.utils.maps.Byte2ObjectMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @param <V> the keyType of elements maintained by this Collection
 * @note Byte2ObjectOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Byte2ObjectOrderedMap directly and will remove Byte2ObjectSortedMap implementations in favor of Byte2ObjectOrderedMap instead
 */
public interface Byte2ObjectSortedMap<V> extends SortedMap<Byte, V>, Byte2ObjectMap<V>
{
	@Override
	public ByteComparator comparator();
	
	@Override
	public Byte2ObjectSortedMap<V> copy();
	
	@Override
	public ByteSortedSet keySet();
	@Override
	public ObjectCollection<V> values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Byte2ObjectMaps#synchronize
	 */
	public default Byte2ObjectSortedMap<V> synchronize() { return Byte2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Byte2ObjectMaps#synchronize
	 */
	public default Byte2ObjectSortedMap<V> synchronize(Object mutex) { return Byte2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Byte2ObjectMaps#unmodifiable
	 */
	public default Byte2ObjectSortedMap<V> unmodifiable() { return Byte2ObjectMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2ObjectSortedMap<V> subMap(byte fromKey, byte toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2ObjectSortedMap<V> headMap(byte toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2ObjectSortedMap<V> tailMap(byte fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public byte firstByteKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public byte pollFirstByteKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public byte lastByteKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public byte pollLastByteKey();
	
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
	public default Byte firstKey() { return Byte.valueOf(firstByteKey()); }
	@Override
	@Deprecated
	public default Byte lastKey() { return Byte.valueOf(lastByteKey()); }
	
	@Override
	@Deprecated
	public default Byte2ObjectSortedMap<V> subMap(Byte fromKey, Byte toKey) { return subMap(fromKey.byteValue(), toKey.byteValue()); }
	@Override
	@Deprecated
	public default Byte2ObjectSortedMap<V> headMap(Byte toKey) { return headMap(toKey.byteValue()); }
	@Override
	@Deprecated
	public default Byte2ObjectSortedMap<V> tailMap(Byte fromKey) { return tailMap(fromKey.byteValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	interface FastSortedSet<V> extends Byte2ObjectMap.FastEntrySet<V>, ObjectSortedSet<Byte2ObjectMap.Entry<V>> {
		@Override
		public ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> fastIterator(byte fromElement);
	}
}