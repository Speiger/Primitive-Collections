package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ShortMapHashCodeTester extends AbstractFloat2ShortMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2ShortMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2ShortMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Short.hashCode(e.getShortValue());
	}
}