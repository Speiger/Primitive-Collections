package speiger.src.testers.shorts.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2ShortMapHashCodeTester extends AbstractShort2ShortMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2ShortMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Short2ShortMap.Entry e) {
		return Short.hashCode(e.getShortKey()) ^ Short.hashCode(e.getShortValue());
	}
}