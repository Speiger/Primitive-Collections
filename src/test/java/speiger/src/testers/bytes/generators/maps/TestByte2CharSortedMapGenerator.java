package speiger.src.testers.bytes.generators.maps;

import com.google.common.collect.testing.TestSortedMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharSortedMap;

@SuppressWarnings("javadoc")
public interface TestByte2CharSortedMapGenerator extends TestByte2CharMapGenerator, TestSortedMapGenerator<Byte, Character>
{
	@Override
	default Byte2CharSortedMap create(Object... elements) { return (Byte2CharSortedMap) TestByte2CharMapGenerator.super.create(elements); }
	@Override
	Byte2CharSortedMap create(Byte2CharMap.Entry... elements);
	
	@Override
	Entry belowSamplesLesser();
	@Override
	Entry belowSamplesGreater();
	@Override
	Entry aboveSamplesLesser();
	@Override
	Entry aboveSamplesGreater();
}