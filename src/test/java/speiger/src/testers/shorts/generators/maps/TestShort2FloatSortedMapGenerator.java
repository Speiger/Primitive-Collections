package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatSortedMap;

public interface TestShort2FloatSortedMapGenerator extends TestShort2FloatMapGenerator, TestSortedMapGenerator<Short, Float>
{
	@Override
	default Short2FloatSortedMap create(Object... elements) { return (Short2FloatSortedMap) TestShort2FloatMapGenerator.super.create(elements); }
	@Override
	Short2FloatSortedMap create(Short2FloatMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}