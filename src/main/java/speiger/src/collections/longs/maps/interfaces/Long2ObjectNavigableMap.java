package speiger.src.collections.longs.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.utils.maps.Long2ObjectMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <V> the type of elements maintained by this Collection
 */
public interface Long2ObjectNavigableMap<V> extends Long2ObjectSortedMap<V>, NavigableMap<Long, V>
{
	@Override
	public Long2ObjectNavigableMap<V> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Long2ObjectNavigableMap<V> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public LongNavigableSet navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public LongNavigableSet descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Long2ObjectMap.Entry<V> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Long2ObjectMap.Entry<V> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Long2ObjectMap.Entry<V> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Long2ObjectMap.Entry<V> pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Long2ObjectMaps#synchronize
	 */
	public default Long2ObjectNavigableMap<V> synchronize() { return Long2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Long2ObjectMaps#synchronize
	 */
	public default Long2ObjectNavigableMap<V> synchronize(Object mutex) { return Long2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Long2ObjectMaps#unmodifiable
	 */
	public default Long2ObjectNavigableMap<V> unmodifiable() { return Long2ObjectMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param fromInclusive if the fromKey is inclusive or not
	 * @param toKey where the subMap should end
	 * @param toInclusive if the toKey is inclusive or not
	 * @return a SubMap that is within the range of the desired range
	 */
	public Long2ObjectNavigableMap<V> subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the HeadMap should end
	 * @param inclusive if the toKey is inclusive or not
	 * @return a HeadMap that is within the range of the desired range
	 */
	public Long2ObjectNavigableMap<V> headMap(long toKey, boolean inclusive);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @param inclusive if the fromKey is inclusive or not
	 * @return a TailMap that is within the range of the desired range
	 */
	public Long2ObjectNavigableMap<V> tailMap(long fromKey, boolean inclusive);
	
	@Override
	public default Long2ObjectNavigableMap<V> subMap(long fromKey, long toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Long2ObjectNavigableMap<V> headMap(long toKey) { return headMap(toKey, false); }
	@Override
	public default Long2ObjectNavigableMap<V> tailMap(long fromKey) { return tailMap(fromKey, true); }
	
	/**
	 * A Helper method to set the max value for SubMaps. (Default: long.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(long e);
	/**
	 * A Helper method to get the max value for SubMaps.
	 * @return the default max value.
	 */
	public long getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubMaps. (Default: long.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(long e);
	/**
	 * A Helper method to get the min value for SubMaps.
	 * @return the default min value.
	 */
	public long getDefaultMinValue();
	
	/**
	 * A Type Specific lowerKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public long lowerKey(long key);
	/**
	 * A Type Specific higherKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public long higherKey(long key);
	/**
	 * A Type Specific floorKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public long floorKey(long key);
	/**
	 * A Type Specific ceilingKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public long ceilingKey(long key);
	
	/**
	 * A Type Specific lowerEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower entry that can be found, or null
	 */
	public Long2ObjectMap.Entry<V> lowerEntry(long key);
	/**
	 * A Type Specific higherEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher entry that can be found, or null
	 */
	public Long2ObjectMap.Entry<V> higherEntry(long key);
	/**
	 * A Type Specific floorEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal entry that can be found, or null
	 */
	public Long2ObjectMap.Entry<V> floorEntry(long key);
	/**
	 * A Type Specific ceilingEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal entry that can be found, or null
	 */
	public Long2ObjectMap.Entry<V> ceilingEntry(long key);
	
	@Override
	@Deprecated
	public default Long lowerKey(Long key) { return Long.valueOf(lowerKey(key.longValue()));}
	@Override
	@Deprecated
	public default Long floorKey(Long key) { return Long.valueOf(floorKey(key.longValue()));}
	@Override
	@Deprecated
	public default Long ceilingKey(Long key) { return Long.valueOf(ceilingKey(key.longValue()));}
	@Override
	@Deprecated
	public default Long higherKey(Long key) { return Long.valueOf(higherKey(key.longValue()));}
	
	@Override
	@Deprecated
	default Long2ObjectMap.Entry<V> lowerEntry(Long key) { return lowerEntry(key.longValue()); }
	@Override
	@Deprecated
	default Long2ObjectMap.Entry<V> floorEntry(Long key) { return floorEntry(key.longValue()); }
	@Override
	@Deprecated
	default Long2ObjectMap.Entry<V> ceilingEntry(Long key) { return ceilingEntry(key.longValue()); }
	@Override
	@Deprecated
	default Long2ObjectMap.Entry<V> higherEntry(Long key) { return higherEntry(key.longValue()); }
	
	@Override
	@Deprecated
	default Long2ObjectNavigableMap<V> subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { return subMap(fromKey.longValue(), fromInclusive, toKey.longValue(), toInclusive); }
	@Override
	@Deprecated
	default Long2ObjectNavigableMap<V> headMap(Long toKey, boolean inclusive) { return headMap(toKey.longValue(), inclusive); }
	@Override
	@Deprecated
	default Long2ObjectNavigableMap<V> tailMap(Long fromKey, boolean inclusive) { return tailMap(fromKey.longValue(), inclusive); }
	@Override
	@Deprecated
	default Long2ObjectNavigableMap<V> subMap(Long fromKey, Long toKey) { return subMap(fromKey.longValue(), true, toKey.longValue(), false); }
	@Override
	@Deprecated
	default Long2ObjectNavigableMap<V> headMap(Long toKey) { return headMap(toKey.longValue(), false); }
	@Override
	@Deprecated
	default Long2ObjectNavigableMap<V> tailMap(Long fromKey) { return tailMap(fromKey.longValue(), true); }
}