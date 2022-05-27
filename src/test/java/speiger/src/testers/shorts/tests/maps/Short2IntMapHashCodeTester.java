package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2IntMapHashCodeTester extends AbstractShort2IntMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2IntMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2IntMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Integer.hashCode(e.getIntValue());
	}
}