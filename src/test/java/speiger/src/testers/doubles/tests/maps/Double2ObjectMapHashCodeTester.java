package speiger.src.testers.doubles.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ObjectMapTester;

@Ignore
public class Double2ObjectMapHashCodeTester<V> extends AbstractDouble2ObjectMapTester<V> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Double2ObjectMap.Entry<V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <V> int hash(Double2ObjectMap.Entry<V> e) {
		return Double.hashCode(e.getDoubleKey()) ^ Objects.hashCode(e.getValue());
	}
}