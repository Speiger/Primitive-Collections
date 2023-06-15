package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2FloatConcurrentMap extends ConcurrentMap<Byte, Float>, Byte2FloatMap
{
	@Override
	@Deprecated
	public default Float compute(Byte key, BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) {
		return Byte2FloatMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfAbsent(Byte key, Function<? super Byte, ? extends Float> mappingFunction) {
		return Byte2FloatMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Float computeIfPresent(Byte key, BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) {
		return Byte2FloatMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Float> action) {
		Byte2FloatMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Float merge(Byte key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) {
		return Byte2FloatMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Float getOrDefault(Object key, Float defaultValue) {
		return Byte2FloatMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Float putIfAbsent(Byte key, Float value) {
		return Byte2FloatMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2FloatMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Float oldValue, Float newValue) {
		return Byte2FloatMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Float replace(Byte key, Float value) {
		return Byte2FloatMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) {
		Byte2FloatMap.super.replaceAll(mappingFunction);
	}
}