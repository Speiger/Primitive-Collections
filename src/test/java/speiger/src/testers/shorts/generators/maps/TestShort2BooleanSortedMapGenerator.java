package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanSortedMap;

public interface TestShort2BooleanSortedMapGenerator extends TestShort2BooleanMapGenerator, TestSortedMapGenerator<Short, Boolean>
{
	@Override
	default Short2BooleanSortedMap create(Object... elements) { return (Short2BooleanSortedMap) TestShort2BooleanMapGenerator.super.create(elements); }
	@Override
	Short2BooleanSortedMap create(Short2BooleanMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}