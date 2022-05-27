package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2LongMapHashCodeTester extends AbstractInt2LongMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2LongMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2LongMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Long.hashCode(e.getLongValue());
	}
}