package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2IntSortedMap;

public interface TestDouble2IntSortedMapGenerator extends TestDouble2IntMapGenerator, TestSortedMapGenerator<Double, Integer>
{
	@Override
	default Double2IntSortedMap create(Object... elements) { return (Double2IntSortedMap) TestDouble2IntMapGenerator.super.create(elements); }
	@Override
	Double2IntSortedMap create(Double2IntMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}