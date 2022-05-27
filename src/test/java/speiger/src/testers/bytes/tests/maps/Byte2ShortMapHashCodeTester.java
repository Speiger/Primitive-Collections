package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ShortMapTester;

@Ignore
public class Byte2ShortMapHashCodeTester extends AbstractByte2ShortMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2ShortMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2ShortMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Short.hashCode(e.getShortValue());
	}
}