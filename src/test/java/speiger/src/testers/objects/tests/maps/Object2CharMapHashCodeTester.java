package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2CharMapTester;

@Ignore
public class Object2CharMapHashCodeTester<T> extends AbstractObject2CharMapTester<T> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2CharMap.Entry<T> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T> int hash(Object2CharMap.Entry<T> e) {
		return Objects.hashCode(e.getKey()) ^ Character.hashCode(e.getCharValue());
	}
}