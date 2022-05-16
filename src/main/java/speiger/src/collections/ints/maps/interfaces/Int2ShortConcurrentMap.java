package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2ShortConcurrentMap extends ConcurrentMap<Integer, Short>, Int2ShortMap
{
	@Override
	@Deprecated
	public default Short compute(Integer key, BiFunction<? super Integer, ? super Short, ? extends Short> mappingFunction) {
		return Int2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfAbsent(Integer key, Function<? super Integer, ? extends Short> mappingFunction) {
		return Int2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfPresent(Integer key, BiFunction<? super Integer, ? super Short, ? extends Short> mappingFunction) {
		return Int2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Short> action) {
		Int2ShortMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Short merge(Integer key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Int2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Int2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Integer key, Short value) {
		return Int2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Short oldValue, Short newValue) {
		return Int2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(Integer key, Short value) {
		return Int2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Short, ? extends Short> mappingFunction) {
		Int2ShortMap.super.replaceAll(mappingFunction);
	}
}