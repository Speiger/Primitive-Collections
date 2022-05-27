package speiger.src.testers.chars.tests.maps;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2ObjectMapTester;

@Ignore
public class Char2ObjectMapHashCodeTester<V> extends AbstractChar2ObjectMapTester<V> {
	public void testHashCode() {
		int expectedHashCode = 0;
		for (Char2ObjectMap.Entry<V> entry : getSampleEntries()) {
			expectedHashCode += hash(entry);
		}
		assertEquals("A Map's hashCode() should be the sum of those of its entries.", expectedHashCode, getMap().hashCode());
	}

	private static <V> int hash(Char2ObjectMap.Entry<V> e) {
		return Character.hashCode(e.getCharKey()) ^ Objects.hashCode(e.getValue());
	}
}