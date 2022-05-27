package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2ShortMapTester;

@Ignore
public class Int2ShortMapHashCodeTester extends AbstractInt2ShortMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2ShortMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2ShortMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Short.hashCode(e.getShortValue());
	}
}