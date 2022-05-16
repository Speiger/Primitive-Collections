package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2FloatConcurrentMap extends ConcurrentMap<Double, Float>, Double2FloatMap
{
	@Override
	@Deprecated
	public default Float compute(Double key, BiFunction<? super Double, ? super Float, ? extends Float> mappingFunction) {
		return Double2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfAbsent(Double key, Function<? super Double, ? extends Float> mappingFunction) {
		return Double2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfPresent(Double key, BiFunction<? super Double, ? super Float, ? extends Float> mappingFunction) {
		return Double2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Float> action) {
		Double2FloatMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Float merge(Double key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Double2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Double2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Double key, Float value) {
		return Double2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Float oldValue, Float newValue) {
		return Double2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(Double key, Float value) {
		return Double2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Float, ? extends Float> mappingFunction) {
		Double2FloatMap.super.replaceAll(mappingFunction);
	}
}