package speiger.src.collections.floats.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.utils.maps.Float2FloatMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Float2FloatOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Float2FloatOrderedMap directly and will remove Float2FloatSortedMap implementations in favor of Float2FloatOrderedMap instead
 */
public interface Float2FloatSortedMap extends SortedMap<Float, Float>, Float2FloatMap
{
	@Override
	public FloatComparator comparator();
	
	@Override
	public Float2FloatSortedMap copy();
	
	@Override
	public FloatSortedSet keySet();
	@Override
	public FloatCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Float2FloatMaps#synchronize
	 */
	public default Float2FloatSortedMap synchronize() { return Float2FloatMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Float2FloatMaps#synchronize
	 */
	public default Float2FloatSortedMap synchronize(Object mutex) { return Float2FloatMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Float2FloatMaps#unmodifiable
	 */
	public default Float2FloatSortedMap unmodifiable() { return Float2FloatMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Float2FloatSortedMap subMap(float fromKey, float toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Float2FloatSortedMap headMap(float toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Float2FloatSortedMap tailMap(float fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public float firstFloatKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public float pollFirstFloatKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public float lastFloatKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public float pollLastFloatKey();
	
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
	public default Float firstKey() { return Float.valueOf(firstFloatKey()); }
	@Override
	@Deprecated
	public default Float lastKey() { return Float.valueOf(lastFloatKey()); }
	
	@Override
	@Deprecated
	public default Float2FloatSortedMap subMap(Float fromKey, Float toKey) { return subMap(fromKey.floatValue(), toKey.floatValue()); }
	@Override
	@Deprecated
	public default Float2FloatSortedMap headMap(Float toKey) { return headMap(toKey.floatValue()); }
	@Override
	@Deprecated
	public default Float2FloatSortedMap tailMap(Float fromKey) { return tailMap(fromKey.floatValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Float2FloatMap.FastEntrySet, ObjectSortedSet<Float2FloatMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Float2FloatMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Float2FloatMap.Entry> fastIterator(float fromElement);
	}
}