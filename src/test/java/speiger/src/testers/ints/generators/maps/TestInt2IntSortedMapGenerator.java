package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2IntSortedMap;

@SuppressWarnings("javadoc")
public interface TestInt2IntSortedMapGenerator extends TestInt2IntMapGenerator, TestSortedMapGenerator<Integer, Integer>
{
	@Override
	default Int2IntSortedMap create(Object... elements) { return (Int2IntSortedMap) TestInt2IntMapGenerator.super.create(elements); }
	@Override
	Int2IntSortedMap create(Int2IntMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}