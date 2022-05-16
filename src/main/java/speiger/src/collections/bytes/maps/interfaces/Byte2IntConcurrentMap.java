package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2IntConcurrentMap extends ConcurrentMap<Byte, Integer>, Byte2IntMap
{
	@Override
	@Deprecated
	public default Integer compute(Byte key, BiFunction<? super Byte, ? super Integer, ? extends Integer> mappingFunction) {
		return Byte2IntMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfAbsent(Byte key, Function<? super Byte, ? extends Integer> mappingFunction) {
		return Byte2IntMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Integer computeIfPresent(Byte key, BiFunction<? super Byte, ? super Integer, ? extends Integer> mappingFunction) {
		return Byte2IntMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Integer> action) {
		Byte2IntMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Integer merge(Byte key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> mappingFunction) {
		return Byte2IntMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Integer getOrDefault(Object key, Integer defaultValue) {
		return Byte2IntMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Integer putIfAbsent(Byte key, Integer value) {
		return Byte2IntMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2IntMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Integer oldValue, Integer newValue) {
		return Byte2IntMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Integer replace(Byte key, Integer value) {
		return Byte2IntMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Integer, ? extends Integer> mappingFunction) {
		Byte2IntMap.super.replaceAll(mappingFunction);
	}
}