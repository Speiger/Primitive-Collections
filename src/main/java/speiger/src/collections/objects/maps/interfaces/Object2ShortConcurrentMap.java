package speiger.src.collections.objects.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 * @param <T> the keyType of elements maintained by this Collection
 */
public interface Object2ShortConcurrentMap<T> extends ConcurrentMap<T, Short>, Object2ShortMap<T>
{
	@Override
	public default Short compute(T key, BiFunction<? super T, ? super Short, ? extends Short> mappingFunction) {
		return Object2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Short computeIfAbsent(T key, Function<? super T, ? extends Short> mappingFunction) {
		return Object2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Short computeIfPresent(T key, BiFunction<? super T, ? super Short, ? extends Short> mappingFunction) {
		return Object2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Short> action) {
		Object2ShortMap.super.forEach(action);
	}

	@Override
	public default Short merge(T key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Object2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Object2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Short putIfAbsent(T key, Short value) {
		return Object2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Short oldValue, Short newValue) {
		return Object2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(T key, Short value) {
		return Object2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Short, ? extends Short> mappingFunction) {
		Object2ShortMap.super.replaceAll(mappingFunction);
	}
}