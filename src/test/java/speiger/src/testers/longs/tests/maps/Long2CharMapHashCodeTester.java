package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2CharMapHashCodeTester extends AbstractLong2CharMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2CharMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2CharMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Character.hashCode(e.getCharValue());
	}
}