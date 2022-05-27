package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2BooleanMapHashCodeTester extends AbstractChar2BooleanMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2BooleanMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2BooleanMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}