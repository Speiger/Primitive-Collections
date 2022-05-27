package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2CharMapTester;

@Ignore
public class Short2CharMapHashCodeTester extends AbstractShort2CharMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2CharMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2CharMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Character.hashCode(e.getCharValue());
	}
}