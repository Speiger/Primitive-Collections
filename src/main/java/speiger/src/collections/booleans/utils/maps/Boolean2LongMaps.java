package speiger.src.collections.booleans.utils.maps;

import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.booleans.maps.interfaces.Boolean2LongMap;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Boolean2LongMaps
{
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Boolean2LongMap.Entry> fastIterator(Boolean2LongMap map) {
		ObjectSet<Boolean2LongMap.Entry> entries = map.boolean2LongEntrySet();
		return entries instanceof Boolean2LongMap.FastEntrySet ? ((Boolean2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Boolean2LongMap.Entry> fastIterable(Boolean2LongMap map) {
		ObjectSet<Boolean2LongMap.Entry> entries = map.boolean2LongEntrySet();
		return map instanceof Boolean2LongMap.FastEntrySet ? new ObjectIterable<Boolean2LongMap.Entry>(){
			@Override
			public ObjectIterator<Boolean2LongMap.Entry> iterator() { return ((Boolean2LongMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Boolean2LongMap.Entry> action) { ((Boolean2LongMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Boolean2LongMap map, Consumer<Boolean2LongMap.Entry> action) {
		ObjectSet<Boolean2LongMap.Entry> entries = map.boolean2LongEntrySet();
		if(entries instanceof Boolean2LongMap.FastEntrySet) ((Boolean2LongMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
}