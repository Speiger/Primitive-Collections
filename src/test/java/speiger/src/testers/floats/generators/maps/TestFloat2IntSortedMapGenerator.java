package speiger.src.testers.floats.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2IntSortedMap;

public interface TestFloat2IntSortedMapGenerator extends TestFloat2IntMapGenerator, TestSortedMapGenerator<Float, Integer>
{
	@Override
	default Float2IntSortedMap create(Object... elements) { return (Float2IntSortedMap) TestFloat2IntMapGenerator.super.create(elements); }
	@Override
	Float2IntSortedMap create(Float2IntMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}