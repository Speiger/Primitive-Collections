package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2DoubleConcurrentMap extends ConcurrentMap<Double, Double>, Double2DoubleMap
{
	@Override
	@Deprecated
	public default Double compute(Double key, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Double2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfAbsent(Double key, Function<? super Double, ? extends Double> mappingFunction) {
		return Double2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfPresent(Double key, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Double2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Double> action) {
		Double2DoubleMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Double merge(Double key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Double2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Double2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Double key, Double value) {
		return Double2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Double oldValue, Double newValue) {
		return Double2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(Double key, Double value) {
		return Double2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		Double2DoubleMap.super.replaceAll(mappingFunction);
	}
}