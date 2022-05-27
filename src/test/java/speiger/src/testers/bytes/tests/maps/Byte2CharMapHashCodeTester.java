package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2CharMapTester;

@Ignore
public class Byte2CharMapHashCodeTester extends AbstractByte2CharMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2CharMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2CharMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Character.hashCode(e.getCharValue());
	}
}