package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2ShortConcurrentMap extends ConcurrentMap<Byte, Short>, Byte2ShortMap
{
	@Override
	@Deprecated
	public default Short compute(Byte key, BiFunction<? super Byte, ? super Short, ? extends Short> mappingFunction) {
		return Byte2ShortMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfAbsent(Byte key, Function<? super Byte, ? extends Short> mappingFunction) {
		return Byte2ShortMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Short computeIfPresent(Byte key, BiFunction<? super Byte, ? super Short, ? extends Short> mappingFunction) {
		return Byte2ShortMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Short> action) {
		Byte2ShortMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Short merge(Byte key, Short value, BiFunction<? super Short, ? super Short, ? extends Short> mappingFunction) {
		return Byte2ShortMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Short getOrDefault(Object key, Short defaultValue) {
		return Byte2ShortMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Short putIfAbsent(Byte key, Short value) {
		return Byte2ShortMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2ShortMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Short oldValue, Short newValue) {
		return Byte2ShortMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Short replace(Byte key, Short value) {
		return Byte2ShortMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Short, ? extends Short> mappingFunction) {
		Byte2ShortMap.super.replaceAll(mappingFunction);
	}
}