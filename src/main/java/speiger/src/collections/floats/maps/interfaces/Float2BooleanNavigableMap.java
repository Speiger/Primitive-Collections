package speiger.src.collections.floats.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.utils.maps.Float2BooleanMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 */
public interface Float2BooleanNavigableMap extends Float2BooleanSortedMap, NavigableMap<Float, Boolean>
{
	@Override
	public Float2BooleanNavigableMap copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Float2BooleanNavigableMap descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public FloatNavigableSet navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public FloatNavigableSet descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Float2BooleanMap.Entry firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Float2BooleanMap.Entry lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Float2BooleanMap.Entry pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Float2BooleanMap.Entry pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Float2BooleanMaps#synchronize
	 */
	public default Float2BooleanNavigableMap synchronize() { return Float2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Float2BooleanMaps#synchronize
	 */
	public default Float2BooleanNavigableMap synchronize(Object mutex) { return Float2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Float2BooleanMaps#unmodifiable
	 */
	public default Float2BooleanNavigableMap unmodifiable() { return Float2BooleanMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param fromInclusive if the fromKey is inclusive or not
	 * @param toKey where the subMap should end
	 * @param toInclusive if the toKey is inclusive or not
	 * @return a SubMap that is within the range of the desired range
	 */
	public Float2BooleanNavigableMap subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the HeadMap should end
	 * @param inclusive if the toKey is inclusive or not
	 * @return a HeadMap that is within the range of the desired range
	 */
	public Float2BooleanNavigableMap headMap(float toKey, boolean inclusive);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @param inclusive if the fromKey is inclusive or not
	 * @return a TailMap that is within the range of the desired range
	 */
	public Float2BooleanNavigableMap tailMap(float fromKey, boolean inclusive);
	
	@Override
	public default Float2BooleanNavigableMap subMap(float fromKey, float toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Float2BooleanNavigableMap headMap(float toKey) { return headMap(toKey, false); }
	@Override
	public default Float2BooleanNavigableMap tailMap(float fromKey) { return tailMap(fromKey, true); }
	
	/**
	 * A Helper method to set the max value for SubMaps. (Default: float.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(float e);
	/**
	 * A Helper method to get the max value for SubMaps.
	 * @return the default max value.
	 */
	public float getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubMaps. (Default: float.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(float e);
	/**
	 * A Helper method to get the min value for SubMaps.
	 * @return the default min value.
	 */
	public float getDefaultMinValue();
	
	/**
	 * A Type Specific lowerKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public float lowerKey(float key);
	/**
	 * A Type Specific higherKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public float higherKey(float key);
	/**
	 * A Type Specific floorKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public float floorKey(float key);
	/**
	 * A Type Specific ceilingKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public float ceilingKey(float key);
	
	/**
	 * A Type Specific lowerEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower entry that can be found, or null
	 */
	public Float2BooleanMap.Entry lowerEntry(float key);
	/**
	 * A Type Specific higherEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher entry that can be found, or null
	 */
	public Float2BooleanMap.Entry higherEntry(float key);
	/**
	 * A Type Specific floorEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal entry that can be found, or null
	 */
	public Float2BooleanMap.Entry floorEntry(float key);
	/**
	 * A Type Specific ceilingEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal entry that can be found, or null
	 */
	public Float2BooleanMap.Entry ceilingEntry(float key);
	
	@Override
	@Deprecated
	public default Float lowerKey(Float key) { return Float.valueOf(lowerKey(key.floatValue()));}
	@Override
	@Deprecated
	public default Float floorKey(Float key) { return Float.valueOf(floorKey(key.floatValue()));}
	@Override
	@Deprecated
	public default Float ceilingKey(Float key) { return Float.valueOf(ceilingKey(key.floatValue()));}
	@Override
	@Deprecated
	public default Float higherKey(Float key) { return Float.valueOf(higherKey(key.floatValue()));}
	
	@Override
	@Deprecated
	default Float2BooleanMap.Entry lowerEntry(Float key) { return lowerEntry(key.floatValue()); }
	@Override
	@Deprecated
	default Float2BooleanMap.Entry floorEntry(Float key) { return floorEntry(key.floatValue()); }
	@Override
	@Deprecated
	default Float2BooleanMap.Entry ceilingEntry(Float key) { return ceilingEntry(key.floatValue()); }
	@Override
	@Deprecated
	default Float2BooleanMap.Entry higherEntry(Float key) { return higherEntry(key.floatValue()); }
	
	@Override
	@Deprecated
	default Float2BooleanNavigableMap subMap(Float fromKey, boolean fromInclusive, Float toKey, boolean toInclusive) { return subMap(fromKey.floatValue(), fromInclusive, toKey.floatValue(), toInclusive); }
	@Override
	@Deprecated
	default Float2BooleanNavigableMap headMap(Float toKey, boolean inclusive) { return headMap(toKey.floatValue(), inclusive); }
	@Override
	@Deprecated
	default Float2BooleanNavigableMap tailMap(Float fromKey, boolean inclusive) { return tailMap(fromKey.floatValue(), inclusive); }
	@Override
	@Deprecated
	default Float2BooleanNavigableMap subMap(Float fromKey, Float toKey) { return subMap(fromKey.floatValue(), true, toKey.floatValue(), false); }
	@Override
	@Deprecated
	default Float2BooleanNavigableMap headMap(Float toKey) { return headMap(toKey.floatValue(), false); }
	@Override
	@Deprecated
	default Float2BooleanNavigableMap tailMap(Float fromKey) { return tailMap(fromKey.floatValue(), true); }
}