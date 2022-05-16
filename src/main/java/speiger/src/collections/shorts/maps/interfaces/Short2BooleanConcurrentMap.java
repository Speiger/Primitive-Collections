package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2BooleanConcurrentMap extends ConcurrentMap<Short, Boolean>, Short2BooleanMap
{
	@Override
	@Deprecated
	public default Boolean compute(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Short2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Short key, Function<? super Short, ? extends Boolean> mappingFunction) {
		return Short2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfPresent(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Short2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Boolean> action) {
		Short2BooleanMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Boolean merge(Short key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Short2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Short2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Short key, Boolean value) {
		return Short2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Boolean oldValue, Boolean newValue) {
		return Short2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Short key, Boolean value) {
		return Short2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Boolean, ? extends Boolean> mappingFunction) {
		Short2BooleanMap.super.replaceAll(mappingFunction);
	}
}