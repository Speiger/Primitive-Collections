package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2LongConcurrentMap extends ConcurrentMap<Double, Long>, Double2LongMap
{
	@Override
	@Deprecated
	public default Long compute(Double key, BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) {
		return Double2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfAbsent(Double key, Function<? super Double, ? extends Long> mappingFunction) {
		return Double2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Long computeIfPresent(Double key, BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) {
		return Double2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Long> action) {
		Double2LongMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Long merge(Double key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Double2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Double2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Long putIfAbsent(Double key, Long value) {
		return Double2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Long oldValue, Long newValue) {
		return Double2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(Double key, Long value) {
		return Double2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Long, ? extends Long> mappingFunction) {
		Double2LongMap.super.replaceAll(mappingFunction);
	}
}