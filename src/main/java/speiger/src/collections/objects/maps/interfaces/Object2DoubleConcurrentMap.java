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
public interface Object2DoubleConcurrentMap<T> extends ConcurrentMap<T, Double>, Object2DoubleMap<T>
{
	@Override
	public default Double compute(T key, BiFunction<? super T, ? super Double, ? extends Double> mappingFunction) {
		return Object2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	public default Double computeIfAbsent(T key, Function<? super T, ? extends Double> mappingFunction) {
		return Object2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public default Double computeIfPresent(T key, BiFunction<? super T, ? super Double, ? extends Double> mappingFunction) {
		return Object2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	public default void forEach(BiConsumer<? super T, ? super Double> action) {
		Object2DoubleMap.super.forEach(action);
	}

	@Override
	public default Double merge(T key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Object2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Object2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public default Double putIfAbsent(T key, Double value) {
		return Object2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Object2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(T key, Double oldValue, Double newValue) {
		return Object2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(T key, Double value) {
		return Object2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super T, ? super Double, ? extends Double> mappingFunction) {
		Object2DoubleMap.super.replaceAll(mappingFunction);
	}
}