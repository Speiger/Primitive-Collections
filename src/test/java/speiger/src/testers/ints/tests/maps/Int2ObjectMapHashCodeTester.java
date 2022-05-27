package speiger.src.testers.ints.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2ObjectMapTester;

@Ignore
public class Int2ObjectMapHashCodeTester<V> extends AbstractInt2ObjectMapTester<V> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Int2ObjectMap.Entry<V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <V> int hash(Int2ObjectMap.Entry<V> e) {
		return Integer.hashCode(e.getIntKey()) ^ Objects.hashCode(e.getValue());
	}
}