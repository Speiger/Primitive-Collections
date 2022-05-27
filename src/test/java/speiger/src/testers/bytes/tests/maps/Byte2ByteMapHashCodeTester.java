package speiger.src.testers.bytes.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2ByteMapHashCodeTester extends AbstractByte2ByteMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2ByteMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Byte2ByteMap.Entry e) {
		return Byte.hashCode(e.getByteKey()) ^ Byte.hashCode(e.getByteValue());
	}
}