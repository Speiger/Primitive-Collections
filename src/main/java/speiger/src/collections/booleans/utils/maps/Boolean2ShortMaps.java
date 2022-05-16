package speiger.src.collections.booleans.utils.maps;

import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.booleans.maps.interfaces.Boolean2ShortMap;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Boolean2ShortMaps
{
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Boolean2ShortMap.Entry> fastIterator(Boolean2ShortMap map) {
		ObjectSet<Boolean2ShortMap.Entry> entries = map.boolean2ShortEntrySet();
		return entries instanceof Boolean2ShortMap.FastEntrySet ? ((Boolean2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Boolean2ShortMap.Entry> fastIterable(Boolean2ShortMap map) {
		ObjectSet<Boolean2ShortMap.Entry> entries = map.boolean2ShortEntrySet();
		return map instanceof Boolean2ShortMap.FastEntrySet ? new ObjectIterable<Boolean2ShortMap.Entry>(){
			@Override
			public ObjectIterator<Boolean2ShortMap.Entry> iterator() { return ((Boolean2ShortMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Boolean2ShortMap.Entry> action) { ((Boolean2ShortMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Boolean2ShortMap map, Consumer<Boolean2ShortMap.Entry> action) {
		ObjectSet<Boolean2ShortMap.Entry> entries = map.boolean2ShortEntrySet();
		if(entries instanceof Boolean2ShortMap.FastEntrySet) ((Boolean2ShortMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
}