package speiger.src.collections.floats.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Float2IntConcurrentMap extends ConcurrentMap<Float, Integer>, Float2IntMap
{
	@Override
	@Deprecated
	public default Integer compute(Float key, BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) {
		return Float2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfAbsent(Float key, Function<? super Float, ? extends Integer> mappingFunction) {
		return Float2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfPresent(Float key, BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) {
		return Float2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Float, ? super Integer> action) {
		Float2IntMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Integer merge(Float key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Float2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Float2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Float key, Integer value) {
		return Float2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Float2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Float key, Integer oldValue, Integer newValue) {
		return Float2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(Float key, Integer value) {
		return Float2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Float, ? super Integer, ? extends Integer> mappingFunction) {
		Float2IntMap.super.replaceAll(mappingFunction);
	}
}