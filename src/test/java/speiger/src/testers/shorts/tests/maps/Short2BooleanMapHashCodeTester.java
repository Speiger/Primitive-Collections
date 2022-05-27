package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2BooleanMapHashCodeTester extends AbstractShort2BooleanMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2BooleanMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2BooleanMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}