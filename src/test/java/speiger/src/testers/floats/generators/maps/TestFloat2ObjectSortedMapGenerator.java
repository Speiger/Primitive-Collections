package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectSortedMap;

@SuppressWarnings("javadoc")
public interface TestFloat2ObjectSortedMapGenerator<V> extends TestFloat2ObjectMapGenerator<V>, TestSortedMapGenerator<Float, V>
{
	@Override
	default Float2ObjectSortedMap<V> create(Object... elements) { return (Float2ObjectSortedMap<V>) TestFloat2ObjectMapGenerator.super.create(elements); }
	@Override
	Float2ObjectSortedMap<V> create(Float2ObjectMap.Entry<V>... elements);
	
	@Override
	Entry<V> belowSamplesLesser();
	@Override
	Entry<V> belowSamplesGreater();
	@Override
	Entry<V> aboveSamplesLesser();
	@Override
	Entry<V> aboveSamplesGreater();
}