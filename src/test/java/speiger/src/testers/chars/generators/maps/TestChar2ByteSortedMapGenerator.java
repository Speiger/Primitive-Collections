package speiger.src.testers.chars.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2ByteSortedMap;

public interface TestChar2ByteSortedMapGenerator extends TestChar2ByteMapGenerator, TestSortedMapGenerator<Character, Byte>
{
	@Override
	default Char2ByteSortedMap create(Object... elements) { return (Char2ByteSortedMap) TestChar2ByteMapGenerator.super.create(elements); }
	@Override
	Char2ByteSortedMap create(Char2ByteMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}