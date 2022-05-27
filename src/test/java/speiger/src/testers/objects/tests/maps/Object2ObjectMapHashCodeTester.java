package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ObjectMapTester;

@Ignore
public class Object2ObjectMapHashCodeTester<T, V> extends AbstractObject2ObjectMapTester<T, V> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2ObjectMap.Entry<T, V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T, V> int hash(Object2ObjectMap.Entry<T, V> e) {
		return Objects.hashCode(e.getKey()) ^ Objects.hashCode(e.getValue());
	}
}