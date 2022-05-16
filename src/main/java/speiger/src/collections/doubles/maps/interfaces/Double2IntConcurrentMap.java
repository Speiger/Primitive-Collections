package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2IntConcurrentMap extends ConcurrentMap<Double, Integer>, Double2IntMap
{
	@Override
	@Deprecated
	public default Integer compute(Double key, BiFunction<? super Double, ? super Integer, ? extends Integer> mappingFunction) {
		return Double2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfAbsent(Double key, Function<? super Double, ? extends Integer> mappingFunction) {
		return Double2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfPresent(Double key, BiFunction<? super Double, ? super Integer, ? extends Integer> mappingFunction) {
		return Double2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Integer> action) {
		Double2IntMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Integer merge(Double key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Double2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Double2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Double key, Integer value) {
		return Double2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Integer oldValue, Integer newValue) {
		return Double2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(Double key, Integer value) {
		return Double2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Integer, ? extends Integer> mappingFunction) {
		Double2IntMap.super.replaceAll(mappingFunction);
	}
}