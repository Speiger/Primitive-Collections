package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2FloatMapTester;

@Ignore
public class Short2FloatMapHashCodeTester extends AbstractShort2FloatMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2FloatMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2FloatMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Float.hashCode(e.getFloatValue());
	}
}