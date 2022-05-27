package speiger.src.testers.shorts.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ObjectMapTester;

@Ignore
public class Short2ObjectMapHashCodeTester<V> extends AbstractShort2ObjectMapTester<V> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Short2ObjectMap.Entry<V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <V> int hash(Short2ObjectMap.Entry<V> e) {
		return Short.hashCode(e.getShortKey()) ^ Objects.hashCode(e.getValue());
	}
}