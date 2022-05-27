package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2LongMapHashCodeTester extends AbstractShort2LongMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2LongMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2LongMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Long.hashCode(e.getLongValue());
	}
}