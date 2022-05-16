package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2IntConcurrentMap extends ConcurrentMap<Integer, Integer>, Int2IntMap
{
	@Override
	@Deprecated
	public default Integer compute(Integer key, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Int2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfAbsent(Integer key, Function<? super Integer, ? extends Integer> mappingFunction) {
		return Int2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfPresent(Integer key, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Int2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Integer> action) {
		Int2IntMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Integer merge(Integer key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Int2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Int2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Integer key, Integer value) {
		return Int2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Integer oldValue, Integer newValue) {
		return Int2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(Integer key, Integer value) {
		return Int2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		Int2IntMap.super.replaceAll(mappingFunction);
	}
}