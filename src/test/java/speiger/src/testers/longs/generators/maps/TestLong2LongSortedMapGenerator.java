package speiger.src.testers.longs.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap.Entry;
import speiger.src.collections.longs.maps.interfaces.Long2LongSortedMap;

public interface TestLong2LongSortedMapGenerator extends TestLong2LongMapGenerator, TestSortedMapGenerator<Long, Long>
{
	@Override
	default Long2LongSortedMap create(Object... elements) { return (Long2LongSortedMap) TestLong2LongMapGenerator.super.create(elements); }
	@Override
	Long2LongSortedMap create(Long2LongMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}