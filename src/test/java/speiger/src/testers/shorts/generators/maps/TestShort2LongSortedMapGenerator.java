package speiger.src.testers.shorts.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2LongSortedMap;

@SuppressWarnings("javadoc")
public interface TestShort2LongSortedMapGenerator extends TestShort2LongMapGenerator, TestSortedMapGenerator<Short, Long>
{
	@Override
	default Short2LongSortedMap create(Object... elements) { return (Short2LongSortedMap) TestShort2LongMapGenerator.super.create(elements); }
	@Override
	Short2LongSortedMap create(Short2LongMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}