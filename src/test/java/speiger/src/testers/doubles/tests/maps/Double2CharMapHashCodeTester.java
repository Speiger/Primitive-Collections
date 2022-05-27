package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2CharMapTester;

@Ignore
public class Double2CharMapHashCodeTester extends AbstractDouble2CharMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2CharMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2CharMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Character.hashCode(e.getCharValue());
	}
}