package speiger.src.testers.ints.tests.maps;


import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2FloatMapHashCodeTester extends AbstractInt2FloatMapTester
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2FloatMap.Entry entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static int hash(Int2FloatMap.Entry e) {
		return Integer.hashCode(e.getIntKey()) ^ Float.hashCode(e.getFloatValue());
	}
}