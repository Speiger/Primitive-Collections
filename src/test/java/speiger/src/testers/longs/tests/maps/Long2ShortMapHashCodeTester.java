package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ShortMapTester;

@Ignore
public class Long2ShortMapHashCodeTester extends AbstractLong2ShortMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2ShortMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2ShortMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Short.hashCode(e.getShortValue());
	}
}