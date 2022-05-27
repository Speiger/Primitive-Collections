package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2CharMapHashCodeTester extends AbstractInt2CharMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2CharMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2CharMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Character.hashCode(e.getCharValue());
	}
}