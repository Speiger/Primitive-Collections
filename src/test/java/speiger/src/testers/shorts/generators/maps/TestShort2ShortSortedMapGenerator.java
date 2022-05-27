package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortSortedMap;

@SuppressWarnings("javadoc")
public interface TestShort2ShortSortedMapGenerator extends TestShort2ShortMapGenerator, TestSortedMapGenerator<Short, Short>
{
	@Override
	default Short2ShortSortedMap create(Object... elements) { return (Short2ShortSortedMap) TestShort2ShortMapGenerator.super.create(elements); }
	@Override
	Short2ShortSortedMap create(Short2ShortMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}