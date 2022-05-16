package speiger.src.collections.objects.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.maps.Object2FloatMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2FloatNavigableMap<T> extends Object2FloatSortedMap<T>, NavigableMap<T, Float>
{
	@Override
	public Object2FloatNavigableMap<T> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Object2FloatNavigableMap<T> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ObjectNavigableSet<T> descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Object2FloatMap.Entry<T> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Object2FloatMap.Entry<T> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Object2FloatMap.Entry<T> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Object2FloatMap.Entry<T> pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Object2FloatMaps#synchronize
	 */
	public default Object2FloatNavigableMap<T> synchronize() { return Object2FloatMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Object2FloatMaps#synchronize
	 */
	public default Object2FloatNavigableMap<T> synchronize(Object mutex) { return Object2FloatMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Object2FloatMaps#unmodifiable
	 */
	public default Object2FloatNavigableMap<T> unmodifiable() { return Object2FloatMaps.unmodifiable(this); }
	
	@Override
	Object2FloatMap.Entry<T> lowerEntry(T key);
	@Override
	Object2FloatMap.Entry<T> floorEntry(T key);
	@Override
	Object2FloatMap.Entry<T> ceilingEntry(T key);
	@Override
	Object2FloatMap.Entry<T> higherEntry(T key);
	
	@Override
	public Object2FloatNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive);
	@Override
	public Object2FloatNavigableMap<T> headMap(T toKey, boolean inclusive);
	@Override
	public Object2FloatNavigableMap<T> tailMap(T fromKey, boolean inclusive);
	@Override
	public default Object2FloatNavigableMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Object2FloatNavigableMap<T> headMap(T toKey) { return headMap(toKey, false); }
	@Override
	public default Object2FloatNavigableMap<T> tailMap(T fromKey) { return tailMap(fromKey, true); }
}