package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2FloatMapHashCodeTester extends AbstractLong2FloatMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2FloatMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2FloatMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Float.hashCode(e.getFloatValue());
	}
}