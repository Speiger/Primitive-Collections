package speiger.src.collections.shorts.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Short2IntConcurrentMap extends ConcurrentMap<Short, Integer>, Short2IntMap
{
	@Override
	@Deprecated
	public default Integer compute(Short key, BiFunction<? super Short, ? super Integer, ? extends Integer> mappingFunction) {
		return Short2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfAbsent(Short key, Function<? super Short, ? extends Integer> mappingFunction) {
		return Short2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfPresent(Short key, BiFunction<? super Short, ? super Integer, ? extends Integer> mappingFunction) {
		return Short2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Integer> action) {
		Short2IntMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Integer merge(Short key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Short2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Short2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Short key, Integer value) {
		return Short2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Short2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Integer oldValue, Integer newValue) {
		return Short2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(Short key, Integer value) {
		return Short2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Integer, ? extends Integer> mappingFunction) {
		Short2IntMap.super.replaceAll(mappingFunction);
	}
}