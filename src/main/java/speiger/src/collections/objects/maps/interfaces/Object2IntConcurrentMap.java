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
public interface Object2IntConcurrentMap<T> extends ConcurrentMap<T, Integer>, Object2IntMap<T>
{
	@Override
	public default Integer compute(T key, BiFunction<? super T, ? super Integer, ? extends Integer> mappingFunction) {
		return Object2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Integer computeIfAbsent(T key, Function<? super T, ? extends Integer> mappingFunction) {
		return Object2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Integer computeIfPresent(T key, BiFunction<? super T, ? super Integer, ? extends Integer> mappingFunction) {
		return Object2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Integer> action) {
		Object2IntMap.super.forEach(action);
	}

	@Override
	public default Integer merge(T key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Object2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Object2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Integer putIfAbsent(T key, Integer value) {
		return Object2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Integer oldValue, Integer newValue) {
		return Object2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(T key, Integer value) {
		return Object2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Integer, ? extends Integer> mappingFunction) {
		Object2IntMap.super.replaceAll(mappingFunction);
	}
}