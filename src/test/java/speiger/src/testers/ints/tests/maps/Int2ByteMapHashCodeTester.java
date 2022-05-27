package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2ByteMapTester;

@Ignore
public class Int2ByteMapHashCodeTester extends AbstractInt2ByteMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2ByteMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2ByteMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Byte.hashCode(e.getByteValue());
	}
}