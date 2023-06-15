package speiger.src.collections.longs.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Long2IntConcurrentMap extends ConcurrentMap<Long, Integer>, Long2IntMap
{
	@Override
	@Deprecated
	public default Integer compute(Long key, BiFunction<? super Long, ? super Integer, ? extends Integer> mappingFunction) {
		return Long2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfAbsent(Long key, Function<? super Long, ? extends Integer> mappingFunction) {
		return Long2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfPresent(Long key, BiFunction<? super Long, ? super Integer, ? extends Integer> mappingFunction) {
		return Long2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Long, ? super Integer> action) {
		Long2IntMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Integer merge(Long key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Long2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Long2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Long key, Integer value) {
		return Long2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Long2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Long key, Integer oldValue, Integer newValue) {
		return Long2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(Long key, Integer value) {
		return Long2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Long, ? super Integer, ? extends Integer> mappingFunction) {
		Long2IntMap.super.replaceAll(mappingFunction);
	}
}