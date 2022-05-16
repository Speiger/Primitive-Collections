package speiger.src.collections.bytes.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.maps.Byte2ByteMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Byte2ByteOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Byte2ByteOrderedMap directly and will remove Byte2ByteSortedMap implementations in favor of Byte2ByteOrderedMap instead
 */
public interface Byte2ByteSortedMap extends SortedMap<Byte, Byte>, Byte2ByteMap
{
	@Override
	public ByteComparator comparator();
	
	@Override
	public Byte2ByteSortedMap copy();
	
	@Override
	public ByteSet keySet();
	@Override
	public ByteCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Byte2ByteMaps#synchronize
	 */
	public default Byte2ByteSortedMap synchronize() { return Byte2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Byte2ByteMaps#synchronize
	 */
	public default Byte2ByteSortedMap synchronize(Object mutex) { return Byte2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Byte2ByteMaps#unmodifiable
	 */
	public default Byte2ByteSortedMap unmodifiable() { return Byte2ByteMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2ByteSortedMap subMap(byte fromKey, byte toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2ByteSortedMap headMap(byte toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2ByteSortedMap tailMap(byte fromKey);
	
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
	public byte firstByteValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public byte lastByteValue();
	
	@Override
	@Deprecated
	public default Byte firstKey() { return Byte.valueOf(firstByteKey()); }
	@Override
	@Deprecated
	public default Byte lastKey() { return Byte.valueOf(lastByteKey()); }
	
	@Override
	@Deprecated
	public default Byte2ByteSortedMap subMap(Byte fromKey, Byte toKey) { return subMap(fromKey.byteValue(), toKey.byteValue()); }
	@Override
	@Deprecated
	public default Byte2ByteSortedMap headMap(Byte toKey) { return headMap(toKey.byteValue()); }
	@Override
	@Deprecated
	public default Byte2ByteSortedMap tailMap(Byte fromKey) { return tailMap(fromKey.byteValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Byte2ByteMap.FastEntrySet, ObjectSortedSet<Byte2ByteMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Byte2ByteMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Byte2ByteMap.Entry> fastIterator(byte fromElement);
	}
}