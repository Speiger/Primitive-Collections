package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectSortedMap;

@SuppressWarnings("javadoc")
public interface TestLong2ObjectSortedMapGenerator<V> extends TestLong2ObjectMapGenerator<V>, TestSortedMapGenerator<Long, V>
{
	@Override
	default Long2ObjectSortedMap<V> create(Object... elements) { return (Long2ObjectSortedMap<V>) TestLong2ObjectMapGenerator.super.create(elements); }
	@Override
	Long2ObjectSortedMap<V> create(Long2ObjectMap.Entry<V>... elements);
	
	@Override
	Entry<V> belowSamplesLesser();
	@Override
	Entry<V> belowSamplesGreater();
	@Override
	Entry<V> aboveSamplesLesser();
	@Override
	Entry<V> aboveSamplesGreater();
}