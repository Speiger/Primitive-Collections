package speiger.src.testers.doubles.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ObjectMapTester;

@Ignore
public class Double2ObjectMapSizeTester<V> extends AbstractDouble2ObjectMapTester<V> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}