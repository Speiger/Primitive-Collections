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
public interface Object2LongConcurrentMap<T> extends ConcurrentMap<T, Long>, Object2LongMap<T>
{
	@Override
	public default Long compute(T key, BiFunction<? super T, ? super Long, ? extends Long> mappingFunction) {
		return Object2LongMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Long computeIfAbsent(T key, Function<? super T, ? extends Long> mappingFunction) {
		return Object2LongMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Long computeIfPresent(T key, BiFunction<? super T, ? super Long, ? extends Long> mappingFunction) {
		return Object2LongMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Long> action) {
		Object2LongMap.super.forEach(action);
	}

	@Override
	public default Long merge(T key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		return Object2LongMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		return Object2LongMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Long putIfAbsent(T key, Long value) {
		return Object2LongMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2LongMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Long oldValue, Long newValue) {
		return Object2LongMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Long replace(T key, Long value) {
		return Object2LongMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Long, ? extends Long> mappingFunction) {
		Object2LongMap.super.replaceAll(mappingFunction);
	}
}