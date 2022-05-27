package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2DoubleMapTester;

@Ignore
public class Char2DoubleMapHashCodeTester extends AbstractChar2DoubleMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2DoubleMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2DoubleMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}