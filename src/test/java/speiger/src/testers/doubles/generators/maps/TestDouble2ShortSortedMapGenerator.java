package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortSortedMap;

public interface TestDouble2ShortSortedMapGenerator extends TestDouble2ShortMapGenerator, TestSortedMapGenerator<Double, Short>
{
	@Override
	default Double2ShortSortedMap create(Object... elements) { return (Double2ShortSortedMap) TestDouble2ShortMapGenerator.super.create(elements); }
	@Override
	Double2ShortSortedMap create(Double2ShortMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}