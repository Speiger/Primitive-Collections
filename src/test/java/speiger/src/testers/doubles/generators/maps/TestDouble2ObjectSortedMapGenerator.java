package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectSortedMap;

public interface TestDouble2ObjectSortedMapGenerator<V> extends TestDouble2ObjectMapGenerator<V>, TestSortedMapGenerator<Double, V>
{
	@Override
	default Double2ObjectSortedMap<V> create(Object... elements) { return (Double2ObjectSortedMap<V>) TestDouble2ObjectMapGenerator.super.create(elements); }
	@Override
	Double2ObjectSortedMap<V> create(Double2ObjectMap.Entry<V>... elements);
	
	@Override
	Entry<V> belowSamplesLesser();
	@Override
	Entry<V> belowSamplesGreater();
	@Override
	Entry<V> aboveSamplesLesser();
	@Override
	Entry<V> aboveSamplesGreater();
}