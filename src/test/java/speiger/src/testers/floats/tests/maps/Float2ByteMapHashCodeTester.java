package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ByteMapTester;

@Ignore
public class Float2ByteMapHashCodeTester extends AbstractFloat2ByteMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2ByteMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2ByteMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Byte.hashCode(e.getByteValue());
	}
}