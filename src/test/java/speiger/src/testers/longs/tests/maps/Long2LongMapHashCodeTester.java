package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2LongMapTester;

@Ignore
public class Long2LongMapHashCodeTester extends AbstractLong2LongMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2LongMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2LongMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Long.hashCode(e.getLongValue());
	}
}