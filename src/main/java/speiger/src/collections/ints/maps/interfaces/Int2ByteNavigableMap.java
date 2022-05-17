package speiger.src.collections.ints.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.utils.maps.Int2ByteMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 */
public interface Int2ByteNavigableMap extends Int2ByteSortedMap, NavigableMap<Integer, Byte>
{
	@Override
	public Int2ByteNavigableMap copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Int2ByteNavigableMap descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public IntNavigableSet navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public IntNavigableSet descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Int2ByteMap.Entry firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Int2ByteMap.Entry lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Int2ByteMap.Entry pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Int2ByteMap.Entry pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Int2ByteMaps#synchronize
	 */
	public default Int2ByteNavigableMap synchronize() { return Int2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Int2ByteMaps#synchronize
	 */
	public default Int2ByteNavigableMap synchronize(Object mutex) { return Int2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Int2ByteMaps#unmodifiable
	 */
	public default Int2ByteNavigableMap unmodifiable() { return Int2ByteMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param fromInclusive if the fromKey is inclusive or not
	 * @param toKey where the subMap should end
	 * @param toInclusive if the toKey is inclusive or not
	 * @return a SubMap that is within the range of the desired range
	 */
	public Int2ByteNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the HeadMap should end
	 * @param inclusive if the toKey is inclusive or not
	 * @return a HeadMap that is within the range of the desired range
	 */
	public Int2ByteNavigableMap headMap(int toKey, boolean inclusive);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @param inclusive if the fromKey is inclusive or not
	 * @return a TailMap that is within the range of the desired range
	 */
	public Int2ByteNavigableMap tailMap(int fromKey, boolean inclusive);
	
	@Override
	public default Int2ByteNavigableMap subMap(int fromKey, int toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Int2ByteNavigableMap headMap(int toKey) { return headMap(toKey, false); }
	@Override
	public default Int2ByteNavigableMap tailMap(int fromKey) { return tailMap(fromKey, true); }
	
	/**
	 * A Helper method to set the max value for SubMaps. (Default: int.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(int e);
	/**
	 * A Helper method to get the max value for SubMaps.
	 * @return the default max value.
	 */
	public int getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubMaps. (Default: int.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(int e);
	/**
	 * A Helper method to get the min value for SubMaps.
	 * @return the default min value.
	 */
	public int getDefaultMinValue();
	
	/**
	 * A Type Specific lowerKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public int lowerKey(int key);
	/**
	 * A Type Specific higherKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public int higherKey(int key);
	/**
	 * A Type Specific floorKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public int floorKey(int key);
	/**
	 * A Type Specific ceilingKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public int ceilingKey(int key);
	
	/**
	 * A Type Specific lowerEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower entry that can be found, or null
	 */
	public Int2ByteMap.Entry lowerEntry(int key);
	/**
	 * A Type Specific higherEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher entry that can be found, or null
	 */
	public Int2ByteMap.Entry higherEntry(int key);
	/**
	 * A Type Specific floorEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal entry that can be found, or null
	 */
	public Int2ByteMap.Entry floorEntry(int key);
	/**
	 * A Type Specific ceilingEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal entry that can be found, or null
	 */
	public Int2ByteMap.Entry ceilingEntry(int key);
	
	@Override
	@Deprecated
	public default Integer lowerKey(Integer key) { return Integer.valueOf(lowerKey(key.intValue()));}
	@Override
	@Deprecated
	public default Integer floorKey(Integer key) { return Integer.valueOf(floorKey(key.intValue()));}
	@Override
	@Deprecated
	public default Integer ceilingKey(Integer key) { return Integer.valueOf(ceilingKey(key.intValue()));}
	@Override
	@Deprecated
	public default Integer higherKey(Integer key) { return Integer.valueOf(higherKey(key.intValue()));}
	
	@Override
	@Deprecated
	default Int2ByteMap.Entry lowerEntry(Integer key) { return lowerEntry(key.intValue()); }
	@Override
	@Deprecated
	default Int2ByteMap.Entry floorEntry(Integer key) { return floorEntry(key.intValue()); }
	@Override
	@Deprecated
	default Int2ByteMap.Entry ceilingEntry(Integer key) { return ceilingEntry(key.intValue()); }
	@Override
	@Deprecated
	default Int2ByteMap.Entry higherEntry(Integer key) { return higherEntry(key.intValue()); }
	
	@Override
	@Deprecated
	default Int2ByteNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return subMap(fromKey.intValue(), fromInclusive, toKey.intValue(), toInclusive); }
	@Override
	@Deprecated
	default Int2ByteNavigableMap headMap(Integer toKey, boolean inclusive) { return headMap(toKey.intValue(), inclusive); }
	@Override
	@Deprecated
	default Int2ByteNavigableMap tailMap(Integer fromKey, boolean inclusive) { return tailMap(fromKey.intValue(), inclusive); }
	@Override
	@Deprecated
	default Int2ByteNavigableMap subMap(Integer fromKey, Integer toKey) { return subMap(fromKey.intValue(), true, toKey.intValue(), false); }
	@Override
	@Deprecated
	default Int2ByteNavigableMap headMap(Integer toKey) { return headMap(toKey.intValue(), false); }
	@Override
	@Deprecated
	default Int2ByteNavigableMap tailMap(Integer fromKey) { return tailMap(fromKey.intValue(), true); }
}