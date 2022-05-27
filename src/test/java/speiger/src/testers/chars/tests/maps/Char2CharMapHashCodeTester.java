package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2CharMapHashCodeTester extends AbstractChar2CharMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2CharMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2CharMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Character.hashCode(e.getCharValue());
	}
}