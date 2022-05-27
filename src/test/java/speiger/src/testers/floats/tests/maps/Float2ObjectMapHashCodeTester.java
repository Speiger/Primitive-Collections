package speiger.src.testers.floats.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ObjectMapHashCodeTester<V> extends AbstractFloat2ObjectMapTester<V>
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Float2ObjectMap.Entry<V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <V> int hash(Float2ObjectMap.Entry<V> e) {
		return Float.hashCode(e.getFloatKey()) ^ Objects.hashCode(e.getValue());
	}
}