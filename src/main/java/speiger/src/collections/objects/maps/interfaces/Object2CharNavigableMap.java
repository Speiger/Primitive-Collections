package speiger.src.collections.objects.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.utils.maps.Object2CharMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2CharNavigableMap<T> extends Object2CharSortedMap<T>, NavigableMap<T, Character>
{
	@Override
	public Object2CharNavigableMap<T> copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Object2CharNavigableMap<T> descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public ObjectNavigableSet<T> navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public ObjectNavigableSet<T> descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Object2CharMap.Entry<T> firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Object2CharMap.Entry<T> lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Object2CharMap.Entry<T> pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Object2CharMap.Entry<T> pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Object2CharMaps#synchronize
	 */
	public default Object2CharNavigableMap<T> synchronize() { return Object2CharMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Object2CharMaps#synchronize
	 */
	public default Object2CharNavigableMap<T> synchronize(Object mutex) { return Object2CharMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Object2CharMaps#unmodifiable
	 */
	public default Object2CharNavigableMap<T> unmodifiable() { return Object2CharMaps.unmodifiable(this); }
	
	@Override
	Object2CharMap.Entry<T> lowerEntry(T key);
	@Override
	Object2CharMap.Entry<T> floorEntry(T key);
	@Override
	Object2CharMap.Entry<T> ceilingEntry(T key);
	@Override
	Object2CharMap.Entry<T> higherEntry(T key);
	
	@Override
	public Object2CharNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive);
	@Override
	public Object2CharNavigableMap<T> headMap(T toKey, boolean inclusive);
	@Override
	public Object2CharNavigableMap<T> tailMap(T fromKey, boolean inclusive);
	@Override
	public default Object2CharNavigableMap<T> subMap(T fromKey, T toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Object2CharNavigableMap<T> headMap(T toKey) { return headMap(toKey, false); }
	@Override
	public default Object2CharNavigableMap<T> tailMap(T fromKey) { return tailMap(fromKey, true); }
}