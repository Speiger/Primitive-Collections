package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2BooleanMapTester;

@Ignore
public class Object2BooleanMapHashCodeTester<T> extends AbstractObject2BooleanMapTester<T> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2BooleanMap.Entry<T> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T> int hash(Object2BooleanMap.Entry<T> e) {
		return Objects.hashCode(e.getKey()) ^ Boolean.hashCode(e.getBooleanValue());
	}
}