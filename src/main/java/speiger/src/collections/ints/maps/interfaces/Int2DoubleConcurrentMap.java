package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2DoubleConcurrentMap extends ConcurrentMap<Integer, Double>, Int2DoubleMap
{
	@Override
	@Deprecated
	public default Double compute(Integer key, BiFunction<? super Integer, ? super Double, ? extends Double> mappingFunction) {
		return Int2DoubleMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfAbsent(Integer key, Function<? super Integer, ? extends Double> mappingFunction) {
		return Int2DoubleMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Double computeIfPresent(Integer key, BiFunction<? super Integer, ? super Double, ? extends Double> mappingFunction) {
		return Int2DoubleMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Double> action) {
		Int2DoubleMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Double merge(Integer key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		return Int2DoubleMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Double getOrDefault(Object key, Double defaultValue) {
		return Int2DoubleMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Integer key, Double value) {
		return Int2DoubleMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2DoubleMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Double oldValue, Double newValue) {
		return Int2DoubleMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Double replace(Integer key, Double value) {
		return Int2DoubleMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Double, ? extends Double> mappingFunction) {
		Int2DoubleMap.super.replaceAll(mappingFunction);
	}
}