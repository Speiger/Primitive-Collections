package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2IntMapHashCodeTester extends AbstractInt2IntMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2IntMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2IntMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Integer.hashCode(e.getIntValue());
	}
}