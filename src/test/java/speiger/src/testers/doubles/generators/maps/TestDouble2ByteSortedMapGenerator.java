package speiger.src.testers.doubles.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap.Entry;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteSortedMap;

public interface TestDouble2ByteSortedMapGenerator extends TestDouble2ByteMapGenerator, TestSortedMapGenerator<Double, Byte>
{
	@Override
	default Double2ByteSortedMap create(Object... elements) { return (Double2ByteSortedMap) TestDouble2ByteMapGenerator.super.create(elements); }
	@Override
	Double2ByteSortedMap create(Double2ByteMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}