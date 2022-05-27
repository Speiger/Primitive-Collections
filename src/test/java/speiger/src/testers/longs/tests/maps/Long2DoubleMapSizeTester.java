package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2DoubleMapSizeTester extends AbstractLong2DoubleMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}