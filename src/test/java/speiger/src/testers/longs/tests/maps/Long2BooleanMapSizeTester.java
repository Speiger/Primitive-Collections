package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2BooleanMapSizeTester extends AbstractLong2BooleanMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}