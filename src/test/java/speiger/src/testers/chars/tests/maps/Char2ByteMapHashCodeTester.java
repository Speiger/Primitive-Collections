package speiger.src.testers.chars.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2ByteMapTester;

@Ignore
public class Char2ByteMapHashCodeTester extends AbstractChar2ByteMapTester {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2ByteMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Char2ByteMap.Entry e) {
		return Character.hashCode(e.getCharKey()) ^ Byte.hashCode(e.getByteValue());
	}
}