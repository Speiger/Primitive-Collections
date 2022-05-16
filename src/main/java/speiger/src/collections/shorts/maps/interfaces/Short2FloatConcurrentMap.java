package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2FloatConcurrentMap extends ConcurrentMap<Short, Float>, Short2FloatMap
{
	@Override
	@Deprecated
	public default Float compute(Short key, BiFunction<? super Short, ? super Float, ? extends Float> mappingFunction) {
		return Short2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfAbsent(Short key, Function<? super Short, ? extends Float> mappingFunction) {
		return Short2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfPresent(Short key, BiFunction<? super Short, ? super Float, ? extends Float> mappingFunction) {
		return Short2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Float> action) {
		Short2FloatMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Float merge(Short key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Short2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Short2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Short key, Float value) {
		return Short2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Float oldValue, Float newValue) {
		return Short2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(Short key, Float value) {
		return Short2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Float, ? extends Float> mappingFunction) {
		Short2FloatMap.super.replaceAll(mappingFunction);
	}
}