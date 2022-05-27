package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2BooleanMapTester;

@Ignore
public class Int2BooleanMapHashCodeTester extends AbstractInt2BooleanMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2BooleanMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2BooleanMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}