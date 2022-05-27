package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2ShortSortedMap;

@SuppressWarnings("javadoc")
public interface TestInt2ShortSortedMapGenerator extends TestInt2ShortMapGenerator, TestSortedMapGenerator<Integer, Short>
{
	@Override
	default Int2ShortSortedMap create(Object... elements) { return (Int2ShortSortedMap) TestInt2ShortMapGenerator.super.create(elements); }
	@Override
	Int2ShortSortedMap create(Int2ShortMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}