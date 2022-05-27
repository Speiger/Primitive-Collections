package speiger.src.testers.floats.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2CharMapHashCodeTester extends AbstractFloat2CharMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2CharMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Float2CharMap.Entry e) {
		return Float.hashCode(e.getFloatKey()) ^ Character.hashCode(e.getCharValue());
	}
}