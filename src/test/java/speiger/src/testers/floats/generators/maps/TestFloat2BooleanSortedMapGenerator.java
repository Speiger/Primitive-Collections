package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanSortedMap;

public interface TestFloat2BooleanSortedMapGenerator extends TestFloat2BooleanMapGenerator, TestSortedMapGenerator<Float, Boolean>
{
	@Override
	default Float2BooleanSortedMap create(Object... elements) { return (Float2BooleanSortedMap) TestFloat2BooleanMapGenerator.super.create(elements); }
	@Override
	Float2BooleanSortedMap create(Float2BooleanMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}