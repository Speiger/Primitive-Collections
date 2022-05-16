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
public interface Object2BooleanConcurrentMap<T> extends ConcurrentMap<T, Boolean>, Object2BooleanMap<T>
{
	@Override
	public default Boolean compute(T key, BiFunction<? super T, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Object2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Boolean computeIfAbsent(T key, Function<? super T, ? extends Boolean> mappingFunction) {
		return Object2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Boolean computeIfPresent(T key, BiFunction<? super T, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Object2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Boolean> action) {
		Object2BooleanMap.super.forEach(action);
	}

	@Override
	public default Boolean merge(T key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Object2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Object2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Boolean putIfAbsent(T key, Boolean value) {
		return Object2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Boolean oldValue, Boolean newValue) {
		return Object2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(T key, Boolean value) {
		return Object2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Boolean, ? extends Boolean> mappingFunction) {
		Object2BooleanMap.super.replaceAll(mappingFunction);
	}
}