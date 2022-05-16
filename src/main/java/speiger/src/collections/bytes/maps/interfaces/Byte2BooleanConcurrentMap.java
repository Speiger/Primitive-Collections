package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2BooleanConcurrentMap extends ConcurrentMap<Byte, Boolean>, Byte2BooleanMap
{
	@Override
	@Deprecated
	public default Boolean compute(Byte key, BiFunction<? super Byte, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Byte2BooleanMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfAbsent(Byte key, Function<? super Byte, ? extends Boolean> mappingFunction) {
		return Byte2BooleanMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Boolean computeIfPresent(Byte key, BiFunction<? super Byte, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Byte2BooleanMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Boolean> action) {
		Byte2BooleanMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Boolean merge(Byte key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> mappingFunction) {
		return Byte2BooleanMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Byte2BooleanMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Boolean putIfAbsent(Byte key, Boolean value) {
		return Byte2BooleanMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2BooleanMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Boolean oldValue, Boolean newValue) {
		return Byte2BooleanMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Boolean replace(Byte key, Boolean value) {
		return Byte2BooleanMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Boolean, ? extends Boolean> mappingFunction) {
		Byte2BooleanMap.super.replaceAll(mappingFunction);
	}
}