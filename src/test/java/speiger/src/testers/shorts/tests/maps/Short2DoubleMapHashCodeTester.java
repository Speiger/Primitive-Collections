package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2DoubleMapTester;

@Ignore
public class Short2DoubleMapHashCodeTester extends AbstractShort2DoubleMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2DoubleMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2DoubleMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}