package speiger.src.collections.booleans.utils.maps;

import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.booleans.maps.interfaces.Boolean2FloatMap;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Boolean2FloatMaps
{
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Boolean2FloatMap.Entry> fastIterator(Boolean2FloatMap map) {
		ObjectSet<Boolean2FloatMap.Entry> entries = map.boolean2FloatEntrySet();
		return entries instanceof Boolean2FloatMap.FastEntrySet ? ((Boolean2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Boolean2FloatMap.Entry> fastIterable(Boolean2FloatMap map) {
		ObjectSet<Boolean2FloatMap.Entry> entries = map.boolean2FloatEntrySet();
		return map instanceof Boolean2FloatMap.FastEntrySet ? new ObjectIterable<Boolean2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Boolean2FloatMap.Entry> iterator() { return ((Boolean2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Boolean2FloatMap.Entry> action) { ((Boolean2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Boolean2FloatMap map, Consumer<Boolean2FloatMap.Entry> action) {
		ObjectSet<Boolean2FloatMap.Entry> entries = map.boolean2FloatEntrySet();
		if(entries instanceof Boolean2FloatMap.FastEntrySet) ((Boolean2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
}