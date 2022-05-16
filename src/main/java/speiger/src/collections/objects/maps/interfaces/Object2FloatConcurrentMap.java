package speiger.src.collections.objects.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2FloatConcurrentMap<T> extends ConcurrentMap<T, Float>, Object2FloatMap<T>
{
	@Override
	public default Float compute(T key, BiFunction<? super T, ? super Float, ? extends Float> mappingFunction) {
		return Object2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Float computeIfAbsent(T key, Function<? super T, ? extends Float> mappingFunction) {
		return Object2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Float computeIfPresent(T key, BiFunction<? super T, ? super Float, ? extends Float> mappingFunction) {
		return Object2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Float> action) {
		Object2FloatMap.super.forEach(action);
	}

	@Override
	public default Float merge(T key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Object2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Object2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Float putIfAbsent(T key, Float value) {
		return Object2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Float oldValue, Float newValue) {
		return Object2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(T key, Float value) {
		return Object2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Float, ? extends Float> mappingFunction) {
		Object2FloatMap.super.replaceAll(mappingFunction);
	}
}