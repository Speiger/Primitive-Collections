package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2LongSortedMap;

public interface TestFloat2LongSortedMapGenerator extends TestFloat2LongMapGenerator, TestSortedMapGenerator<Float, Long>
{
	@Override
	default Float2LongSortedMap create(Object... elements) { return (Float2LongSortedMap) TestFloat2LongMapGenerator.super.create(elements); }
	@Override
	Float2LongSortedMap create(Float2LongMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}