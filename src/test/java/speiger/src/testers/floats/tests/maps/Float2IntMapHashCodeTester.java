package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2IntMapHashCodeTester extends AbstractFloat2IntMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2IntMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2IntMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Integer.hashCode(e.getIntValue());
	}
}