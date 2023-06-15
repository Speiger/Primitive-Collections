package speiger.src.collections.objects.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.maps.Object2ShortMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface Object2ShortNavigableMap<T> extends Object2ShortSortedMap<T>, NavigableMap<T, Short>
{
	@Override
	public Object2ShortNavigableMap<T> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Object2ShortNavigableMap<T> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ObjectNavigableSet<T> descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Object2ShortMap.Entry<T> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Object2ShortMap.Entry<T> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Object2ShortMap.Entry<T> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Object2ShortMap.Entry<T> pollLastEntry();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> keySet();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Object2ShortMaps#synchronize
	 */
	public default Object2ShortNavigableMap<T> synchronize() { return Object2ShortMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Object2ShortMaps#synchronize
	 */
	public default Object2ShortNavigableMap<T> synchronize(Object mutex) { return Object2ShortMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Object2ShortMaps#unmodifiable
	 */
	public default Object2ShortNavigableMap<T> unmodifiable() { return Object2ShortMaps.unmodifiable(this); }
	
	@Override
	Object2ShortMap.Entry<T> lowerEntry(T key);
	@Override
	Object2ShortMap.Entry<T> floorEntry(T key);
	@Override
	Object2ShortMap.Entry<T> ceilingEntry(T key);
	@Override
	Object2ShortMap.Entry<T> higherEntry(T key);
	
	@Override
	public Object2ShortNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive);
	@Override
	public Object2ShortNavigableMap<T> headMap(T toKey, boolean inclusive);
	@Override
	public Object2ShortNavigableMap<T> tailMap(T fromKey, boolean inclusive);
	@Override
	public default Object2ShortNavigableMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Object2ShortNavigableMap<T> headMap(T toKey) { return headMap(toKey, false); }
	@Override
	public default Object2ShortNavigableMap<T> tailMap(T fromKey) { return tailMap(fromKey, true); }
}