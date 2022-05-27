package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2DoubleMapTester;

@Ignore
public class Double2DoubleMapHashCodeTester extends AbstractDouble2DoubleMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2DoubleMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2DoubleMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}