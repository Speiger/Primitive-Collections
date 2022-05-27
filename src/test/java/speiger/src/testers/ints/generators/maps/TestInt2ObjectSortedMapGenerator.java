package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectSortedMap;

public interface TestInt2ObjectSortedMapGenerator<V> extends TestInt2ObjectMapGenerator<V>, TestSortedMapGenerator<Integer, V>
{
	@Override
	default Int2ObjectSortedMap<V> create(Object... elements) { return (Int2ObjectSortedMap<V>) TestInt2ObjectMapGenerator.super.create(elements); }
	@Override
	Int2ObjectSortedMap<V> create(Int2ObjectMap.Entry<V>... elements);
	
	@Override
	Entry<V> belowSamplesLesser();
	@Override
	Entry<V> belowSamplesGreater();
	@Override
	Entry<V> aboveSamplesLesser();
	@Override
	Entry<V> aboveSamplesGreater();
}