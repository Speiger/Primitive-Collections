package speiger.src.testers.bytes.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ObjectMapTester;

@Ignore
public class Byte2ObjectMapHashCodeTester<V> extends AbstractByte2ObjectMapTester<V> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Byte2ObjectMap.Entry<V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <V> int hash(Byte2ObjectMap.Entry<V> e) {
		return Byte.hashCode(e.getByteKey()) ^ Objects.hashCode(e.getValue());
	}
}