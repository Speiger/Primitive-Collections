package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectSortedMap;

public interface TestShort2ObjectSortedMapGenerator<V> extends TestShort2ObjectMapGenerator<V>, TestSortedMapGenerator<Short, V>
{
	@Override
	default Short2ObjectSortedMap<V> create(Object... elements) { return (Short2ObjectSortedMap<V>) TestShort2ObjectMapGenerator.super.create(elements); }
	@Override
	Short2ObjectSortedMap<V> create(Short2ObjectMap.Entry<V>... elements);
	
	@Override
	Entry<V> belowSamplesLesser();
	@Override
	Entry<V> belowSamplesGreater();
	@Override
	Entry<V> aboveSamplesLesser();
	@Override
	Entry<V> aboveSamplesGreater();
}