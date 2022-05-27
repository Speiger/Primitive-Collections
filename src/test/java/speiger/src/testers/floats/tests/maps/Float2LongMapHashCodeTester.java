package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2LongMapTester;

@Ignore
public class Float2LongMapHashCodeTester extends AbstractFloat2LongMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2LongMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2LongMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Long.hashCode(e.getLongValue());
	}
}