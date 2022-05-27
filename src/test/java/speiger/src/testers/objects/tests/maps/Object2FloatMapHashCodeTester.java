package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2FloatMapTester;

@Ignore
public class Object2FloatMapHashCodeTester<T> extends AbstractObject2FloatMapTester<T> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2FloatMap.Entry<T> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T> int hash(Object2FloatMap.Entry<T> e) {
		return Objects.hashCode(e.getKey()) ^ Float.hashCode(e.getFloatValue());
	}
}