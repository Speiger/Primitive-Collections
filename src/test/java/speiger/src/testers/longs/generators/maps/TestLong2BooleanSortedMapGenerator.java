package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanSortedMap;

@SuppressWarnings("javadoc")
public interface TestLong2BooleanSortedMapGenerator extends TestLong2BooleanMapGenerator, TestSortedMapGenerator<Long, Boolean>
{
	@Override
	default Long2BooleanSortedMap create(Object... elements) { return (Long2BooleanSortedMap) TestLong2BooleanMapGenerator.super.create(elements); }
	@Override
	Long2BooleanSortedMap create(Long2BooleanMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}