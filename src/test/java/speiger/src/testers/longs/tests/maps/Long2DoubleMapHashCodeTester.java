package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2DoubleMapHashCodeTester extends AbstractLong2DoubleMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2DoubleMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2DoubleMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}