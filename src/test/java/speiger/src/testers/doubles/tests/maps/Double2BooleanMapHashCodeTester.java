package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2BooleanMapTester;

@Ignore
public class Double2BooleanMapHashCodeTester extends AbstractDouble2BooleanMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2BooleanMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2BooleanMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}