package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2FloatMapTester;

@Ignore
public class Char2FloatMapHashCodeTester extends AbstractChar2FloatMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2FloatMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2FloatMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Float.hashCode(e.getFloatValue());
	}
}