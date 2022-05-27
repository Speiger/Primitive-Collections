package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ObjectMapSizeTester<V> extends AbstractLong2ObjectMapTester<V> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}