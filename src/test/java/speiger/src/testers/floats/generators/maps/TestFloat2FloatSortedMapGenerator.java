package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2FloatSortedMap;

@SuppressWarnings("javadoc")
public interface TestFloat2FloatSortedMapGenerator extends TestFloat2FloatMapGenerator, TestSortedMapGenerator<Float, Float>
{
	@Override
	default Float2FloatSortedMap create(Object... elements) { return (Float2FloatSortedMap) TestFloat2FloatMapGenerator.super.create(elements); }
	@Override
	Float2FloatSortedMap create(Float2FloatMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}