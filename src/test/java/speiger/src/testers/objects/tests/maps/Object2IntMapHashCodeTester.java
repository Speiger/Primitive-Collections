package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2IntMapTester;

@Ignore
public class Object2IntMapHashCodeTester<T> extends AbstractObject2IntMapTester<T> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2IntMap.Entry<T> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T> int hash(Object2IntMap.Entry<T> e) {
		return Objects.hashCode(e.getKey()) ^ Integer.hashCode(e.getIntValue());
	}
}