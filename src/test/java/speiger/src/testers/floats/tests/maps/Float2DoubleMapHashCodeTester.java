package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2DoubleMapHashCodeTester extends AbstractFloat2DoubleMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2DoubleMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2DoubleMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}