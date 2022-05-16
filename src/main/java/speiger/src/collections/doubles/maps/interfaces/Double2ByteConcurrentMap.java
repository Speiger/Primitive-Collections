package speiger.src.collections.doubles.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Double2ByteConcurrentMap extends ConcurrentMap<Double, Byte>, Double2ByteMap
{
	@Override
	@Deprecated
	public default Byte compute(Double key, BiFunction<? super Double, ? super Byte, ? extends Byte> mappingFunction) {
		return Double2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfAbsent(Double key, Function<? super Double, ? extends Byte> mappingFunction) {
		return Double2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfPresent(Double key, BiFunction<? super Double, ? super Byte, ? extends Byte> mappingFunction) {
		return Double2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Double, ? super Byte> action) {
		Double2ByteMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Byte merge(Double key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Double2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Double2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Double key, Byte value) {
		return Double2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Double2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Double key, Byte oldValue, Byte newValue) {
		return Double2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(Double key, Byte value) {
		return Double2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Double, ? super Byte, ? extends Byte> mappingFunction) {
		Double2ByteMap.super.replaceAll(mappingFunction);
	}
}