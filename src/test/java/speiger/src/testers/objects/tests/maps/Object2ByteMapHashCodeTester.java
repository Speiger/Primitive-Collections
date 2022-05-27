package speiger.src.testers.objects.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ByteMapHashCodeTester<T> extends AbstractObject2ByteMapTester<T>
{
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Object2ByteMap.Entry<T> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <T> int hash(Object2ByteMap.Entry<T> e) {
		return Objects.hashCode(e.getKey()) ^ Byte.hashCode(e.getByteValue());
	}
}