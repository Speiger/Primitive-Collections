package speiger.src.collections.objects.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.maps.Object2ByteMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2ByteNavigableMap<T> extends Object2ByteSortedMap<T>, NavigableMap<T, Byte>
{
	@Override
	public Object2ByteNavigableMap<T> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Object2ByteNavigableMap<T> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ObjectNavigableSet<T> descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Object2ByteMap.Entry<T> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Object2ByteMap.Entry<T> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Object2ByteMap.Entry<T> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Object2ByteMap.Entry<T> pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Object2ByteMaps#synchronize
	 */
	public default Object2ByteNavigableMap<T> synchronize() { return Object2ByteMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Object2ByteMaps#synchronize
	 */
	public default Object2ByteNavigableMap<T> synchronize(Object mutex) { return Object2ByteMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Object2ByteMaps#unmodifiable
	 */
	public default Object2ByteNavigableMap<T> unmodifiable() { return Object2ByteMaps.unmodifiable(this); }
	
	@Override
	Object2ByteMap.Entry<T> lowerEntry(T key);
	@Override
	Object2ByteMap.Entry<T> floorEntry(T key);
	@Override
	Object2ByteMap.Entry<T> ceilingEntry(T key);
	@Override
	Object2ByteMap.Entry<T> higherEntry(T key);
	
	@Override
	public Object2ByteNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive);
	@Override
	public Object2ByteNavigableMap<T> headMap(T toKey, boolean inclusive);
	@Override
	public Object2ByteNavigableMap<T> tailMap(T fromKey, boolean inclusive);
	@Override
	public default Object2ByteNavigableMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Object2ByteNavigableMap<T> headMap(T toKey) { return headMap(toKey, false); }
	@Override
	public default Object2ByteNavigableMap<T> tailMap(T fromKey) { return tailMap(fromKey, true); }
}