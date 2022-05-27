package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2IntMapTester;

@Ignore
public class Char2IntMapHashCodeTester extends AbstractChar2IntMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2IntMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2IntMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Integer.hashCode(e.getIntValue());
	}
}