package speiger.src.testers.ints.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleSortedMap;

public interface TestInt2DoubleSortedMapGenerator extends TestInt2DoubleMapGenerator, TestSortedMapGenerator<Integer, Double>
{
	@Override
	default Int2DoubleSortedMap create(Object... elements) { return (Int2DoubleSortedMap) TestInt2DoubleMapGenerator.super.create(elements); }
	@Override
	Int2DoubleSortedMap create(Int2DoubleMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}