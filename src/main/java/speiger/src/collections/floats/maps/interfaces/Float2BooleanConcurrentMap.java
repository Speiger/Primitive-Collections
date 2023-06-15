package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2BooleanConcurrentMap extends ConcurrentMap<Float, Boolean>, Float2BooleanMap
{
	@Override
	@Deprecated
	public default Boolean compute(Float key, BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Float2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Float key, Function<? super Float, ? extends Boolean> mappingFunction) {
		return Float2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfPresent(Float key, BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Float2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Boolean> action) {
		Float2BooleanMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Boolean merge(Float key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Float2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Float2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Float key, Boolean value) {
		return Float2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Boolean oldValue, Boolean newValue) {
		return Float2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Float key, Boolean value) {
		return Float2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Boolean, ? extends Boolean> mappingFunction) {
		Float2BooleanMap.super.replaceAll(mappingFunction);
	}
}