package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2BooleanConcurrentMap extends ConcurrentMap<Double, Boolean>, Double2BooleanMap
{
	@Override
	@Deprecated
	public default Boolean compute(Double key, BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Double2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Double key, Function<? super Double, ? extends Boolean> mappingFunction) {
		return Double2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfPresent(Double key, BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Double2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Boolean> action) {
		Double2BooleanMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Boolean merge(Double key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Double2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Double2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Double key, Boolean value) {
		return Double2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Boolean oldValue, Boolean newValue) {
		return Double2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Double key, Boolean value) {
		return Double2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Boolean, ? extends Boolean> mappingFunction) {
		Double2BooleanMap.super.replaceAll(mappingFunction);
	}
}