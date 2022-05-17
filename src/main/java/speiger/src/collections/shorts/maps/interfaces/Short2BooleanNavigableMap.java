package speiger.src.collections.shorts.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.utils.maps.Short2BooleanMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 */
public interface Short2BooleanNavigableMap extends Short2BooleanSortedMap, NavigableMap<Short, Boolean>
{
	@Override
	public Short2BooleanNavigableMap copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Short2BooleanNavigableMap descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ShortNavigableSet navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ShortNavigableSet descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Short2BooleanMap.Entry firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Short2BooleanMap.Entry lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Short2BooleanMap.Entry pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Short2BooleanMap.Entry pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Short2BooleanMaps#synchronize
	 */
	public default Short2BooleanNavigableMap synchronize() { return Short2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Short2BooleanMaps#synchronize
	 */
	public default Short2BooleanNavigableMap synchronize(Object mutex) { return Short2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Short2BooleanMaps#unmodifiable
	 */
	public default Short2BooleanNavigableMap unmodifiable() { return Short2BooleanMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param fromInclusive if the fromKey is inclusive or not
	 * @param toKey where the subMap should end
	 * @param toInclusive if the toKey is inclusive or not
	 * @return a SubMap that is within the range of the desired range
	 */
	public Short2BooleanNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the HeadMap should end
	 * @param inclusive if the toKey is inclusive or not
	 * @return a HeadMap that is within the range of the desired range
	 */
	public Short2BooleanNavigableMap headMap(short toKey, boolean inclusive);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @param inclusive if the fromKey is inclusive or not
	 * @return a TailMap that is within the range of the desired range
	 */
	public Short2BooleanNavigableMap tailMap(short fromKey, boolean inclusive);
	
	@Override
	public default Short2BooleanNavigableMap subMap(short fromKey, short toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Short2BooleanNavigableMap headMap(short toKey) { return headMap(toKey, false); }
	@Override
	public default Short2BooleanNavigableMap tailMap(short fromKey) { return tailMap(fromKey, true); }
	
	/**
	 * A Helper method to set the max value for SubMaps. (Default: short.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(short e);
	/**
	 * A Helper method to get the max value for SubMaps.
	 * @return the default max value.
	 */
	public short getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubMaps. (Default: short.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(short e);
	/**
	 * A Helper method to get the min value for SubMaps.
	 * @return the default min value.
	 */
	public short getDefaultMinValue();
	
	/**
	 * A Type Specific lowerKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public short lowerKey(short key);
	/**
	 * A Type Specific higherKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public short higherKey(short key);
	/**
	 * A Type Specific floorKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public short floorKey(short key);
	/**
	 * A Type Specific ceilingKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public short ceilingKey(short key);
	
	/**
	 * A Type Specific lowerEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower entry that can be found, or null
	 */
	public Short2BooleanMap.Entry lowerEntry(short key);
	/**
	 * A Type Specific higherEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher entry that can be found, or null
	 */
	public Short2BooleanMap.Entry higherEntry(short key);
	/**
	 * A Type Specific floorEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal entry that can be found, or null
	 */
	public Short2BooleanMap.Entry floorEntry(short key);
	/**
	 * A Type Specific ceilingEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal entry that can be found, or null
	 */
	public Short2BooleanMap.Entry ceilingEntry(short key);
	
	@Override
	@Deprecated
	public default Short lowerKey(Short key) { return Short.valueOf(lowerKey(key.shortValue()));}
	@Override
	@Deprecated
	public default Short floorKey(Short key) { return Short.valueOf(floorKey(key.shortValue()));}
	@Override
	@Deprecated
	public default Short ceilingKey(Short key) { return Short.valueOf(ceilingKey(key.shortValue()));}
	@Override
	@Deprecated
	public default Short higherKey(Short key) { return Short.valueOf(higherKey(key.shortValue()));}
	
	@Override
	@Deprecated
	default Short2BooleanMap.Entry lowerEntry(Short key) { return lowerEntry(key.shortValue()); }
	@Override
	@Deprecated
	default Short2BooleanMap.Entry floorEntry(Short key) { return floorEntry(key.shortValue()); }
	@Override
	@Deprecated
	default Short2BooleanMap.Entry ceilingEntry(Short key) { return ceilingEntry(key.shortValue()); }
	@Override
	@Deprecated
	default Short2BooleanMap.Entry higherEntry(Short key) { return higherEntry(key.shortValue()); }
	
	@Override
	@Deprecated
	default Short2BooleanNavigableMap subMap(Short fromKey, boolean fromInclusive, Short toKey, boolean toInclusive) { return subMap(fromKey.shortValue(), fromInclusive, toKey.shortValue(), toInclusive); }
	@Override
	@Deprecated
	default Short2BooleanNavigableMap headMap(Short toKey, boolean inclusive) { return headMap(toKey.shortValue(), inclusive); }
	@Override
	@Deprecated
	default Short2BooleanNavigableMap tailMap(Short fromKey, boolean inclusive) { return tailMap(fromKey.shortValue(), inclusive); }
	@Override
	@Deprecated
	default Short2BooleanNavigableMap subMap(Short fromKey, Short toKey) { return subMap(fromKey.shortValue(), true, toKey.shortValue(), false); }
	@Override
	@Deprecated
	default Short2BooleanNavigableMap headMap(Short toKey) { return headMap(toKey.shortValue(), false); }
	@Override
	@Deprecated
	default Short2BooleanNavigableMap tailMap(Short fromKey) { return tailMap(fromKey.shortValue(), true); }
}