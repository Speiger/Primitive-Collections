package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2ShortSortedMap;

public interface TestLong2ShortSortedMapGenerator extends TestLong2ShortMapGenerator, TestSortedMapGenerator<Long, Short>
{
	@Override
	default Long2ShortSortedMap create(Object... elements) { return (Long2ShortSortedMap) TestLong2ShortMapGenerator.super.create(elements); }
	@Override
	Long2ShortSortedMap create(Long2ShortMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}