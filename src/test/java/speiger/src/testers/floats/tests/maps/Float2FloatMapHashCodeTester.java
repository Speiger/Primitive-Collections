package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2FloatMapTester;

@Ignore
public class Float2FloatMapHashCodeTester extends AbstractFloat2FloatMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2FloatMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2FloatMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Float.hashCode(e.getFloatValue());
	}
}