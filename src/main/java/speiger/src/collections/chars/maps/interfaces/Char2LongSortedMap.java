package speiger.src.collections.chars.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2LongMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Char2LongOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Char2LongOrderedMap directly and will remove Char2LongSortedMap implementations in favor of Char2LongOrderedMap instead
 */
public interface Char2LongSortedMap extends SortedMap<Character, Long>, Char2LongMap
{
	@Override
	public CharComparator comparator();
	
	@Override
	public Char2LongSortedMap copy();
	
	@Override
	public CharSet keySet();
	@Override
	public LongCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Char2LongMaps#synchronize
	 */
	public default Char2LongSortedMap synchronize() { return Char2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Char2LongMaps#synchronize
	 */
	public default Char2LongSortedMap synchronize(Object mutex) { return Char2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Char2LongMaps#unmodifiable
	 */
	public default Char2LongSortedMap unmodifiable() { return Char2LongMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Char2LongSortedMap subMap(char fromKey, char toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Char2LongSortedMap headMap(char toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Char2LongSortedMap tailMap(char fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public char firstCharKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public char pollFirstCharKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public char lastCharKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public char pollLastCharKey();
	
	/**
	 * A method to get the first Value of a Map.
	 * @return the first key in the map
	 */
	public long firstLongValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public long lastLongValue();
	
	@Override
	@Deprecated
	public default Character firstKey() { return Character.valueOf(firstCharKey()); }
	@Override
	@Deprecated
	public default Character lastKey() { return Character.valueOf(lastCharKey()); }
	
	@Override
	@Deprecated
	public default Char2LongSortedMap subMap(Character fromKey, Character toKey) { return subMap(fromKey.charValue(), toKey.charValue()); }
	@Override
	@Deprecated
	public default Char2LongSortedMap headMap(Character toKey) { return headMap(toKey.charValue()); }
	@Override
	@Deprecated
	public default Char2LongSortedMap tailMap(Character fromKey) { return tailMap(fromKey.charValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Char2LongMap.FastEntrySet, ObjectSortedSet<Char2LongMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Char2LongMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Char2LongMap.Entry> fastIterator(char fromElement);
	}
}