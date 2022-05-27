package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2BooleanMapHashCodeTester extends AbstractByte2BooleanMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2BooleanMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2BooleanMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}