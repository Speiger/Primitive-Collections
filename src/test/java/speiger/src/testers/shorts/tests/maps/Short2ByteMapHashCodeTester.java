package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2ByteMapHashCodeTester extends AbstractShort2ByteMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2ByteMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2ByteMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Byte.hashCode(e.getByteValue());
	}
}