package speiger.src.testers.doubles.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2ShortMapHashCodeTester extends AbstractDouble2ShortMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2ShortMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Double2ShortMap.Entry e) {
		return Double.hashCode(e.getDoubleKey()) ^ Short.hashCode(e.getShortValue());
	}
}