package speiger.src.collections.ints.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Int2FloatConcurrentMap extends ConcurrentMap<Integer, Float>, Int2FloatMap
{
	@Override
	@Deprecated
	public default Float compute(Integer key, BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) {
		return Int2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfAbsent(Integer key, Function<? super Integer, ? extends Float> mappingFunction) {
		return Int2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfPresent(Integer key, BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) {
		return Int2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Integer, ? super Float> action) {
		Int2FloatMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Float merge(Integer key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Int2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Int2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Integer key, Float value) {
		return Int2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Int2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Integer key, Float oldValue, Float newValue) {
		return Int2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(Integer key, Float value) {
		return Int2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Integer, ? super Float, ? extends Float> mappingFunction) {
		Int2FloatMap.super.replaceAll(mappingFunction);
	}
}