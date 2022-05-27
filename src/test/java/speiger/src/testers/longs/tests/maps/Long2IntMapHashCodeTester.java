package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2IntMapTester;

@Ignore
public class Long2IntMapHashCodeTester extends AbstractLong2IntMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2IntMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2IntMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Integer.hashCode(e.getIntValue());
	}
}