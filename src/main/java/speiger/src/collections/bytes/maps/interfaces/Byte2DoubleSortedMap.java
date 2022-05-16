package speiger.src.collections.bytes.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.maps.Byte2DoubleMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Byte2DoubleOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Byte2DoubleOrderedMap directly and will remove Byte2DoubleSortedMap implementations in favor of Byte2DoubleOrderedMap instead
 */
public interface Byte2DoubleSortedMap extends SortedMap<Byte, Double>, Byte2DoubleMap
{
	@Override
	public ByteComparator comparator();
	
	@Override
	public Byte2DoubleSortedMap copy();
	
	@Override
	public ByteSet keySet();
	@Override
	public DoubleCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Byte2DoubleMaps#synchronize
	 */
	public default Byte2DoubleSortedMap synchronize() { return Byte2DoubleMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Byte2DoubleMaps#synchronize
	 */
	public default Byte2DoubleSortedMap synchronize(Object mutex) { return Byte2DoubleMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Byte2DoubleMaps#unmodifiable
	 */
	public default Byte2DoubleSortedMap unmodifiable() { return Byte2DoubleMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2DoubleSortedMap subMap(byte fromKey, byte toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2DoubleSortedMap headMap(byte toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Byte2DoubleSortedMap tailMap(byte fromKey);
	
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
	public double firstDoubleValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public double lastDoubleValue();
	
	@Override
	@Deprecated
	public default Byte firstKey() { return Byte.valueOf(firstByteKey()); }
	@Override
	@Deprecated
	public default Byte lastKey() { return Byte.valueOf(lastByteKey()); }
	
	@Override
	@Deprecated
	public default Byte2DoubleSortedMap subMap(Byte fromKey, Byte toKey) { return subMap(fromKey.byteValue(), toKey.byteValue()); }
	@Override
	@Deprecated
	public default Byte2DoubleSortedMap headMap(Byte toKey) { return headMap(toKey.byteValue()); }
	@Override
	@Deprecated
	public default Byte2DoubleSortedMap tailMap(Byte fromKey) { return tailMap(fromKey.byteValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Byte2DoubleMap.FastEntrySet, ObjectSortedSet<Byte2DoubleMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Byte2DoubleMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Byte2DoubleMap.Entry> fastIterator(byte fromElement);
	}
}