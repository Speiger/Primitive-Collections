package speiger.src.testers.ints.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2ObjectMapSizeTester<V> extends AbstractInt2ObjectMapTester<V> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}