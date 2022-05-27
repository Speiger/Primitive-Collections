package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2DoubleMapTester;

@Ignore
public class Object2DoubleMapHashCodeTester<T> extends AbstractObject2DoubleMapTester<T> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2DoubleMap.Entry<T> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T> int hash(Object2DoubleMap.Entry<T> e) {
		return Objects.hashCode(e.getKey()) ^ Double.hashCode(e.getDoubleValue());
	}
}