package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2LongMapTester;

@Ignore
public class Double2LongMapHashCodeTester extends AbstractDouble2LongMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2LongMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2LongMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Long.hashCode(e.getLongValue());
	}
}