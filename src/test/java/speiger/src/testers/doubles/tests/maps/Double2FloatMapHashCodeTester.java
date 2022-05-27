package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2FloatMapTester;

@Ignore
public class Double2FloatMapHashCodeTester extends AbstractDouble2FloatMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2FloatMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2FloatMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Float.hashCode(e.getFloatValue());
	}
}