package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2ShortMapHashCodeTester extends AbstractChar2ShortMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2ShortMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2ShortMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Short.hashCode(e.getShortValue());
	}
}