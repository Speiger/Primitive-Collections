package speiger.src.collections.objects.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.maps.Object2IntMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2IntNavigableMap<T> extends Object2IntSortedMap<T>, NavigableMap<T, Integer>
{
	@Override
	public Object2IntNavigableMap<T> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Object2IntNavigableMap<T> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ObjectNavigableSet<T> descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Object2IntMap.Entry<T> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Object2IntMap.Entry<T> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Object2IntMap.Entry<T> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Object2IntMap.Entry<T> pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Object2IntMaps#synchronize
	 */
	public default Object2IntNavigableMap<T> synchronize() { return Object2IntMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Object2IntMaps#synchronize
	 */
	public default Object2IntNavigableMap<T> synchronize(Object mutex) { return Object2IntMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Object2IntMaps#unmodifiable
	 */
	public default Object2IntNavigableMap<T> unmodifiable() { return Object2IntMaps.unmodifiable(this); }
	
	@Override
	Object2IntMap.Entry<T> lowerEntry(T key);
	@Override
	Object2IntMap.Entry<T> floorEntry(T key);
	@Override
	Object2IntMap.Entry<T> ceilingEntry(T key);
	@Override
	Object2IntMap.Entry<T> higherEntry(T key);
	
	@Override
	public Object2IntNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive);
	@Override
	public Object2IntNavigableMap<T> headMap(T toKey, boolean inclusive);
	@Override
	public Object2IntNavigableMap<T> tailMap(T fromKey, boolean inclusive);
	@Override
	public default Object2IntNavigableMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Object2IntNavigableMap<T> headMap(T toKey) { return headMap(toKey, false); }
	@Override
	public default Object2IntNavigableMap<T> tailMap(T fromKey) { return tailMap(fromKey, true); }
}