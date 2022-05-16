package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2BooleanConcurrentMap extends ConcurrentMap<Integer, Boolean>, Int2BooleanMap
{
	@Override
	@Deprecated
	public default Boolean compute(Integer key, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Int2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Integer key, Function<? super Integer, ? extends Boolean> mappingFunction) {
		return Int2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfPresent(Integer key, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Int2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Boolean> action) {
		Int2BooleanMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Boolean merge(Integer key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Int2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Int2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Integer key, Boolean value) {
		return Int2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Boolean oldValue, Boolean newValue) {
		return Int2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Integer key, Boolean value) {
		return Int2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Boolean, ? extends Boolean> mappingFunction) {
		Int2BooleanMap.super.replaceAll(mappingFunction);
	}
}