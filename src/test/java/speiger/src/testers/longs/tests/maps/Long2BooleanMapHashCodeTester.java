package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2BooleanMapHashCodeTester extends AbstractLong2BooleanMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2BooleanMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2BooleanMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}