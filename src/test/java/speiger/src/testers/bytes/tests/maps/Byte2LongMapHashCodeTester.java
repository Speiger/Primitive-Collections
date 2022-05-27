package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2LongMapTester;

@Ignore
public class Byte2LongMapHashCodeTester extends AbstractByte2LongMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2LongMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2LongMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Long.hashCode(e.getLongValue());
	}
}