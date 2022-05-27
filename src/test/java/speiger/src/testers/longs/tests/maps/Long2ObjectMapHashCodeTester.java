package speiger.src.testers.longs.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ObjectMapTester;

@Ignore
public class Long2ObjectMapHashCodeTester<V> extends AbstractLong2ObjectMapTester<V> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Long2ObjectMap.Entry<V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <V> int hash(Long2ObjectMap.Entry<V> e) {
		return Long.hashCode(e.getLongKey()) ^ Objects.hashCode(e.getValue());
	}
}