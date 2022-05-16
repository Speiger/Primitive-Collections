package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2FloatConcurrentMap extends ConcurrentMap<Float, Float>, Float2FloatMap
{
	@Override
	@Deprecated
	public default Float compute(Float key, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Float2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfAbsent(Float key, Function<? super Float, ? extends Float> mappingFunction) {
		return Float2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfPresent(Float key, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Float2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Float> action) {
		Float2FloatMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Float merge(Float key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Float2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Float2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Float key, Float value) {
		return Float2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Float oldValue, Float newValue) {
		return Float2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(Float key, Float value) {
		return Float2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		Float2FloatMap.super.replaceAll(mappingFunction);
	}
}