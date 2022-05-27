package speiger.src.testers.longs.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ByteMapTester;

@Ignore
public class Long2ByteMapHashCodeTester extends AbstractLong2ByteMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2ByteMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Long2ByteMap.Entry e) {
		return Long.hashCode(e.getLongKey()) ^ Byte.hashCode(e.getByteValue());
	}
}