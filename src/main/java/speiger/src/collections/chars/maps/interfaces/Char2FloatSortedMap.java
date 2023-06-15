package speiger.src.collections.chars.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.utils.maps.Char2FloatMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Char2FloatOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Char2FloatOrderedMap directly and will remove Char2FloatSortedMap implementations in favor of Char2FloatOrderedMap instead
 */
public interface Char2FloatSortedMap extends SortedMap<Character, Float>, Char2FloatMap
{
	@Override
	public CharComparator comparator();
	
	@Override
	public Char2FloatSortedMap copy();
	
	@Override
	public CharSortedSet keySet();
	@Override
	public FloatCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Char2FloatMaps#synchronize
	 */
	public default Char2FloatSortedMap synchronize() { return Char2FloatMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Char2FloatMaps#synchronize
	 */
	public default Char2FloatSortedMap synchronize(Object mutex) { return Char2FloatMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Char2FloatMaps#unmodifiable
	 */
	public default Char2FloatSortedMap unmodifiable() { return Char2FloatMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Char2FloatSortedMap subMap(char fromKey, char toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Char2FloatSortedMap headMap(char toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Char2FloatSortedMap tailMap(char fromKey);
	
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
	public float firstFloatValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public float lastFloatValue();
	
	@Override
	@Deprecated
	public default Character firstKey() { return Character.valueOf(firstCharKey()); }
	@Override
	@Deprecated
	public default Character lastKey() { return Character.valueOf(lastCharKey()); }
	
	@Override
	@Deprecated
	public default Char2FloatSortedMap subMap(Character fromKey, Character toKey) { return subMap(fromKey.charValue(), toKey.charValue()); }
	@Override
	@Deprecated
	public default Char2FloatSortedMap headMap(Character toKey) { return headMap(toKey.charValue()); }
	@Override
	@Deprecated
	public default Char2FloatSortedMap tailMap(Character fromKey) { return tailMap(fromKey.charValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Char2FloatMap.FastEntrySet, ObjectSortedSet<Char2FloatMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Char2FloatMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Char2FloatMap.Entry> fastIterator(char fromElement);
	}
}