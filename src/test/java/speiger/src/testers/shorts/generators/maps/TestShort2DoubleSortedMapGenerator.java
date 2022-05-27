package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleSortedMap;

public interface TestShort2DoubleSortedMapGenerator extends TestShort2DoubleMapGenerator, TestSortedMapGenerator<Short, Double>
{
	@Override
	default Short2DoubleSortedMap create(Object... elements) { return (Short2DoubleSortedMap) TestShort2DoubleMapGenerator.super.create(elements); }
	@Override
	Short2DoubleSortedMap create(Short2DoubleMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}