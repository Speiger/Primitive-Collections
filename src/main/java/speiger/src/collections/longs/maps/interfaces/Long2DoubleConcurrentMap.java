package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2DoubleConcurrentMap extends ConcurrentMap<Long, Double>, Long2DoubleMap
{
	@Override
	@Deprecated
	public default Double compute(Long key, BiFunction<? super Long, ? super Double, ? extends Double> mappingFunction) {
		return Long2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfAbsent(Long key, Function<? super Long, ? extends Double> mappingFunction) {
		return Long2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfPresent(Long key, BiFunction<? super Long, ? super Double, ? extends Double> mappingFunction) {
		return Long2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Double> action) {
		Long2DoubleMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Double merge(Long key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Long2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Long2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Long key, Double value) {
		return Long2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Double oldValue, Double newValue) {
		return Long2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(Long key, Double value) {
		return Long2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Double, ? extends Double> mappingFunction) {
		Long2DoubleMap.super.replaceAll(mappingFunction);
	}
}