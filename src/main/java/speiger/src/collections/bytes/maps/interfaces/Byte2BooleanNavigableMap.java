package speiger.src.collections.bytes.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.utils.maps.Byte2BooleanMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 */
public interface Byte2BooleanNavigableMap extends Byte2BooleanSortedMap, NavigableMap<Byte, Boolean>
{
	@Override
	public Byte2BooleanNavigableMap copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Byte2BooleanNavigableMap descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ByteNavigableSet navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ByteNavigableSet descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Byte2BooleanMap.Entry firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Byte2BooleanMap.Entry lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Byte2BooleanMap.Entry pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Byte2BooleanMap.Entry pollLastEntry();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ByteNavigableSet keySet();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Byte2BooleanMaps#synchronize
	 */
	public default Byte2BooleanNavigableMap synchronize() { return Byte2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Byte2BooleanMaps#synchronize
	 */
	public default Byte2BooleanNavigableMap synchronize(Object mutex) { return Byte2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Byte2BooleanMaps#unmodifiable
	 */
	public default Byte2BooleanNavigableMap unmodifiable() { return Byte2BooleanMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param fromInclusive if the fromKey is inclusive or not
	 * @param toKey where the subMap should end
	 * @param toInclusive if the toKey is inclusive or not
	 * @return a SubMap that is within the range of the desired range
	 */
	public Byte2BooleanNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the HeadMap should end
	 * @param inclusive if the toKey is inclusive or not
	 * @return a HeadMap that is within the range of the desired range
	 */
	public Byte2BooleanNavigableMap headMap(byte toKey, boolean inclusive);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @param inclusive if the fromKey is inclusive or not
	 * @return a TailMap that is within the range of the desired range
	 */
	public Byte2BooleanNavigableMap tailMap(byte fromKey, boolean inclusive);
	
	@Override
	public default Byte2BooleanNavigableMap subMap(byte fromKey, byte toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Byte2BooleanNavigableMap headMap(byte toKey) { return headMap(toKey, false); }
	@Override
	public default Byte2BooleanNavigableMap tailMap(byte fromKey) { return tailMap(fromKey, true); }
	
	/**
	 * A Helper method to set the max value for SubMaps. (Default: byte.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(byte e);
	/**
	 * A Helper method to get the max value for SubMaps.
	 * @return the default max value.
	 */
	public byte getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubMaps. (Default: byte.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(byte e);
	/**
	 * A Helper method to get the min value for SubMaps.
	 * @return the default min value.
	 */
	public byte getDefaultMinValue();
	
	/**
	 * A Type Specific lowerKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public byte lowerKey(byte key);
	/**
	 * A Type Specific higherKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public byte higherKey(byte key);
	/**
	 * A Type Specific floorKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public byte floorKey(byte key);
	/**
	 * A Type Specific ceilingKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public byte ceilingKey(byte key);
	
	/**
	 * A Type Specific lowerEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower entry that can be found, or null
	 */
	public Byte2BooleanMap.Entry lowerEntry(byte key);
	/**
	 * A Type Specific higherEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher entry that can be found, or null
	 */
	public Byte2BooleanMap.Entry higherEntry(byte key);
	/**
	 * A Type Specific floorEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal entry that can be found, or null
	 */
	public Byte2BooleanMap.Entry floorEntry(byte key);
	/**
	 * A Type Specific ceilingEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal entry that can be found, or null
	 */
	public Byte2BooleanMap.Entry ceilingEntry(byte key);
	
	@Override
	@Deprecated
	public default Byte lowerKey(Byte key) { return Byte.valueOf(lowerKey(key.byteValue()));}
	@Override
	@Deprecated
	public default Byte floorKey(Byte key) { return Byte.valueOf(floorKey(key.byteValue()));}
	@Override
	@Deprecated
	public default Byte ceilingKey(Byte key) { return Byte.valueOf(ceilingKey(key.byteValue()));}
	@Override
	@Deprecated
	public default Byte higherKey(Byte key) { return Byte.valueOf(higherKey(key.byteValue()));}
	
	@Override
	@Deprecated
	default Byte2BooleanMap.Entry lowerEntry(Byte key) { return lowerEntry(key.byteValue()); }
	@Override
	@Deprecated
	default Byte2BooleanMap.Entry floorEntry(Byte key) { return floorEntry(key.byteValue()); }
	@Override
	@Deprecated
	default Byte2BooleanMap.Entry ceilingEntry(Byte key) { return ceilingEntry(key.byteValue()); }
	@Override
	@Deprecated
	default Byte2BooleanMap.Entry higherEntry(Byte key) { return higherEntry(key.byteValue()); }
	
	@Override
	@Deprecated
	default Byte2BooleanNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { return subMap(fromKey.byteValue(), fromInclusive, toKey.byteValue(), toInclusive); }
	@Override
	@Deprecated
	default Byte2BooleanNavigableMap headMap(Byte toKey, boolean inclusive) { return headMap(toKey.byteValue(), inclusive); }
	@Override
	@Deprecated
	default Byte2BooleanNavigableMap tailMap(Byte fromKey, boolean inclusive) { return tailMap(fromKey.byteValue(), inclusive); }
	@Override
	@Deprecated
	default Byte2BooleanNavigableMap subMap(Byte fromKey, Byte toKey) { return subMap(fromKey.byteValue(), true, toKey.byteValue(), false); }
	@Override
	@Deprecated
	default Byte2BooleanNavigableMap headMap(Byte toKey) { return headMap(toKey.byteValue(), false); }
	@Override
	@Deprecated
	default Byte2BooleanNavigableMap tailMap(Byte fromKey) { return tailMap(fromKey.byteValue(), true); }
}