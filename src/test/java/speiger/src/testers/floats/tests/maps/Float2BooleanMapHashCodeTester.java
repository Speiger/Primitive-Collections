package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2BooleanMapHashCodeTester extends AbstractFloat2BooleanMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2BooleanMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2BooleanMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}