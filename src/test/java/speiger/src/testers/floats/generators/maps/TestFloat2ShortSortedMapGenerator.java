package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2ShortSortedMap;

public interface TestFloat2ShortSortedMapGenerator extends TestFloat2ShortMapGenerator, TestSortedMapGenerator<Float, Short>
{
	@Override
	default Float2ShortSortedMap create(Object... elements) { return (Float2ShortSortedMap) TestFloat2ShortMapGenerator.super.create(elements); }
	@Override
	Float2ShortSortedMap create(Float2ShortMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}