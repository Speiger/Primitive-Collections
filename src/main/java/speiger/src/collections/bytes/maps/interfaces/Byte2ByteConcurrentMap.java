package speiger.src.collections.bytes.maps.interfaces;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A type specific ConcurrentMap interface that reduces boxing/unboxing.
 * Since the interface adds nothing new. It is there just for completion sake.
 */
public interface Byte2ByteConcurrentMap extends ConcurrentMap<Byte, Byte>, Byte2ByteMap
{
	@Override
	@Deprecated
	public default Byte compute(Byte key, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Byte2ByteMap.super.compute(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfAbsent(Byte key, Function<? super Byte, ? extends Byte> mappingFunction) {
		return Byte2ByteMap.super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default Byte computeIfPresent(Byte key, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Byte2ByteMap.super.computeIfPresent(key, mappingFunction);
	}

	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Byte, ? super Byte> action) {
		Byte2ByteMap.super.forEach(action);
	}

	@Override
	@Deprecated
	public default Byte merge(Byte key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		return Byte2ByteMap.super.merge(key, value, mappingFunction);
	}
	
	@Deprecated
	@Override
	public default Byte getOrDefault(Object key, Byte defaultValue) {
		return Byte2ByteMap.super.getOrDefault(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public default Byte putIfAbsent(Byte key, Byte value) {
		return Byte2ByteMap.super.putIfAbsent(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object key, Object value) {
		return Byte2ByteMap.super.remove(key, value);
	}
	
	@Override
	@Deprecated
	public default boolean replace(Byte key, Byte oldValue, Byte newValue) {
		return Byte2ByteMap.super.replace(key, oldValue, newValue);
	}
	
	@Override
	@Deprecated
	public default Byte replace(Byte key, Byte value) {
		return Byte2ByteMap.super.replace(key, value);
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) {
		Byte2ByteMap.super.replaceAll(mappingFunction);
	}
}