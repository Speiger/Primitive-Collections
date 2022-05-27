package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2LongMapHashCodeTester extends AbstractChar2LongMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2LongMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2LongMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Long.hashCode(e.getLongValue());
	}
}