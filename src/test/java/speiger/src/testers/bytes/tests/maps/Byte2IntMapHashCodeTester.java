package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2IntMapTester;

@Ignore
public class Byte2IntMapHashCodeTester extends AbstractByte2IntMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2IntMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2IntMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Integer.hashCode(e.getIntValue());
	}
}