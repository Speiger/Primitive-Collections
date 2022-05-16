package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2LongConcurrentMap extends ConcurrentMap<Float, Long>, Float2LongMap
{
	@Override
	@Deprecated
	public default Long compute(Float key, BiFunction<? super Float, ? super Long, ? extends Long> mappingFunction) {
		return Float2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfAbsent(Float key, Function<? super Float, ? extends Long> mappingFunction) {
		return Float2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfPresent(Float key, BiFunction<? super Float, ? super Long, ? extends Long> mappingFunction) {
		return Float2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Long> action) {
		Float2LongMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Long merge(Float key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Float2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Float2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Float key, Long value) {
		return Float2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Long oldValue, Long newValue) {
		return Float2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(Float key, Long value) {
		return Float2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Long, ? extends Long> mappingFunction) {
		Float2LongMap.super.replaceAll(mappingFunction);
	}
}