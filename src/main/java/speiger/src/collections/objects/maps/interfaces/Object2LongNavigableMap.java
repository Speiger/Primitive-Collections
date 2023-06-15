package speiger.src.collections.objects.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.maps.Object2LongMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface Object2LongNavigableMap<T> extends Object2LongSortedMap<T>, NavigableMap<T, Long>
{
	@Override
	public Object2LongNavigableMap<T> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Object2LongNavigableMap<T> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ObjectNavigableSet<T> descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Object2LongMap.Entry<T> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Object2LongMap.Entry<T> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Object2LongMap.Entry<T> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Object2LongMap.Entry<T> pollLastEntry();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> keySet();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	public default Object2LongNavigableMap<T> synchronize() { return Object2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	public default Object2LongNavigableMap<T> synchronize(Object mutex) { return Object2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Object2LongMaps#unmodifiable
	 */
	public default Object2LongNavigableMap<T> unmodifiable() { return Object2LongMaps.unmodifiable(this); }
	
	@Override
	Object2LongMap.Entry<T> lowerEntry(T key);
	@Override
	Object2LongMap.Entry<T> floorEntry(T key);
	@Override
	Object2LongMap.Entry<T> ceilingEntry(T key);
	@Override
	Object2LongMap.Entry<T> higherEntry(T key);
	
	@Override
	public Object2LongNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive);
	@Override
	public Object2LongNavigableMap<T> headMap(T toKey, boolean inclusive);
	@Override
	public Object2LongNavigableMap<T> tailMap(T fromKey, boolean inclusive);
	@Override
	public default Object2LongNavigableMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Object2LongNavigableMap<T> headMap(T toKey) { return headMap(toKey, false); }
	@Override
	public default Object2LongNavigableMap<T> tailMap(T fromKey) { return tailMap(fromKey, true); }
}