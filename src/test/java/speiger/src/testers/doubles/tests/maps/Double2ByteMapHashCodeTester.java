package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ByteMapTester;

@Ignore
public class Double2ByteMapHashCodeTester extends AbstractDouble2ByteMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2ByteMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2ByteMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Byte.hashCode(e.getByteValue());
	}
}