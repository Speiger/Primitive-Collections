package speiger.src.collections.objects.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.maps.Object2ObjectMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <T> the type of elements maintained by this Collection
 * @param <V> the type of elements maintained by this Collection
 */
public interface Object2ObjectNavigableMap<T, V> extends Object2ObjectSortedMap<T, V>, NavigableMap<T, V>
{
	@Override
	public Object2ObjectNavigableMap<T, V> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Object2ObjectNavigableMap<T, V> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ObjectNavigableSet<T> descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Object2ObjectMap.Entry<T, V> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Object2ObjectMap.Entry<T, V> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Object2ObjectMap.Entry<T, V> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Object2ObjectMap.Entry<T, V> pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Object2ObjectMaps#synchronize
	 */
	public default Object2ObjectNavigableMap<T, V> synchronize() { return Object2ObjectMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Object2ObjectMaps#synchronize
	 */
	public default Object2ObjectNavigableMap<T, V> synchronize(Object mutex) { return Object2ObjectMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Object2ObjectMaps#unmodifiable
	 */
	public default Object2ObjectNavigableMap<T, V> unmodifiable() { return Object2ObjectMaps.unmodifiable(this); }
	
	@Override
	Object2ObjectMap.Entry<T, V> lowerEntry(T key);
	@Override
	Object2ObjectMap.Entry<T, V> floorEntry(T key);
	@Override
	Object2ObjectMap.Entry<T, V> ceilingEntry(T key);
	@Override
	Object2ObjectMap.Entry<T, V> higherEntry(T key);
	
	@Override
	public Object2ObjectNavigableMap<T, V> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive);
	@Override
	public Object2ObjectNavigableMap<T, V> headMap(T toKey, boolean inclusive);
	@Override
	public Object2ObjectNavigableMap<T, V> tailMap(T fromKey, boolean inclusive);
	@Override
	public default Object2ObjectNavigableMap<T, V> subMap(T fromKey, T toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Object2ObjectNavigableMap<T, V> headMap(T toKey) { return headMap(toKey, false); }
	@Override
	public default Object2ObjectNavigableMap<T, V> tailMap(T fromKey) { return tailMap(fromKey, true); }
}