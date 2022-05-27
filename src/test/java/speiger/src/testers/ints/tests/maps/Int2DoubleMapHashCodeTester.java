package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2DoubleMapTester;

@Ignore
public class Int2DoubleMapHashCodeTester extends AbstractInt2DoubleMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2DoubleMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2DoubleMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}