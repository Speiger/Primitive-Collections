package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleSortedMap;

public interface TestFloat2DoubleSortedMapGenerator extends TestFloat2DoubleMapGenerator, TestSortedMapGenerator<Float, Double>
{
	@Override
	default Float2DoubleSortedMap create(Object... elements) { return (Float2DoubleSortedMap) TestFloat2DoubleMapGenerator.super.create(elements); }
	@Override
	Float2DoubleSortedMap create(Float2DoubleMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}