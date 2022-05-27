package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2IntMapHashCodeTester extends AbstractDouble2IntMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2IntMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2IntMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Integer.hashCode(e.getIntValue());
	}
}