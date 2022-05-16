package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2FloatConcurrentMap extends ConcurrentMap<Long, Float>, Long2FloatMap
{
	@Override
	@Deprecated
	public default Float compute(Long key, BiFunction<? super Long, ? super Float, ? extends Float> mappingFunction) {
		return Long2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfAbsent(Long key, Function<? super Long, ? extends Float> mappingFunction) {
		return Long2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfPresent(Long key, BiFunction<? super Long, ? super Float, ? extends Float> mappingFunction) {
		return Long2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Float> action) {
		Long2FloatMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Float merge(Long key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Long2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Long2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Long key, Float value) {
		return Long2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Float oldValue, Float newValue) {
		return Long2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(Long key, Float value) {
		return Long2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Float, ? extends Float> mappingFunction) {
		Long2FloatMap.super.replaceAll(mappingFunction);
	}
}