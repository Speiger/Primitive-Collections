package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2DoubleMapHashCodeTester extends AbstractByte2DoubleMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2DoubleMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2DoubleMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}