package speiger.src.collections.booleans.utils.maps;

import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.booleans.maps.interfaces.Boolean2ObjectMap;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Boolean2ObjectMaps
{
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Boolean2ObjectMap.Entry<V>> fastIterator(Boolean2ObjectMap<V> map) {
		ObjectSet<Boolean2ObjectMap.Entry<V>> entries = map.boolean2ObjectEntrySet();
		return entries instanceof Boolean2ObjectMap.FastEntrySet ? ((Boolean2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Boolean2ObjectMap.Entry<V>> fastIterable(Boolean2ObjectMap<V> map) {
		ObjectSet<Boolean2ObjectMap.Entry<V>> entries = map.boolean2ObjectEntrySet();
		return map instanceof Boolean2ObjectMap.FastEntrySet ? new ObjectIterable<Boolean2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Boolean2ObjectMap.Entry<V>> iterator() { return ((Boolean2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Boolean2ObjectMap.Entry<V>> action) { ((Boolean2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Boolean2ObjectMap<V> map, Consumer<Boolean2ObjectMap.Entry<V>> action) {
		ObjectSet<Boolean2ObjectMap.Entry<V>> entries = map.boolean2ObjectEntrySet();
		if(entries instanceof Boolean2ObjectMap.FastEntrySet) ((Boolean2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
}