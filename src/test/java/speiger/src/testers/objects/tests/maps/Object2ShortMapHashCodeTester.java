package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ShortMapHashCodeTester<T> extends AbstractObject2ShortMapTester<T>
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2ShortMap.Entry<T> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T> int hash(Object2ShortMap.Entry<T> e) {
		return Objects.hashCode(e.getKey()) ^ Short.hashCode(e.getShortValue());
	}
}