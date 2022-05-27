package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2LongSortedMap;

@SuppressWarnings("javadoc")
public interface TestDouble2LongSortedMapGenerator extends TestDouble2LongMapGenerator, TestSortedMapGenerator<Double, Long>
{
	@Override
	default Double2LongSortedMap create(Object... elements) { return (Double2LongSortedMap) TestDouble2LongMapGenerator.super.create(elements); }
	@Override
	Double2LongSortedMap create(Double2LongMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}