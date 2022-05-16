package speiger.src.collections.booleans.utils.maps;

import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.booleans.maps.interfaces.Boolean2DoubleMap;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Boolean2DoubleMaps
{
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Boolean2DoubleMap.Entry> fastIterator(Boolean2DoubleMap map) {
		ObjectSet<Boolean2DoubleMap.Entry> entries = map.boolean2DoubleEntrySet();
		return entries instanceof Boolean2DoubleMap.FastEntrySet ? ((Boolean2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Boolean2DoubleMap.Entry> fastIterable(Boolean2DoubleMap map) {
		ObjectSet<Boolean2DoubleMap.Entry> entries = map.boolean2DoubleEntrySet();
		return map instanceof Boolean2DoubleMap.FastEntrySet ? new ObjectIterable<Boolean2DoubleMap.Entry>(){
			@Override
			public ObjectIterator<Boolean2DoubleMap.Entry> iterator() { return ((Boolean2DoubleMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Boolean2DoubleMap.Entry> action) { ((Boolean2DoubleMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Boolean2DoubleMap map, Consumer<Boolean2DoubleMap.Entry> action) {
		ObjectSet<Boolean2DoubleMap.Entry> entries = map.boolean2DoubleEntrySet();
		if(entries instanceof Boolean2DoubleMap.FastEntrySet) ((Boolean2DoubleMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
}