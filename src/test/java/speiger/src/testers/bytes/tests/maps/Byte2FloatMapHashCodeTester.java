package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2FloatMapHashCodeTester extends AbstractByte2FloatMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2FloatMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2FloatMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Float.hashCode(e.getFloatValue());
	}
}