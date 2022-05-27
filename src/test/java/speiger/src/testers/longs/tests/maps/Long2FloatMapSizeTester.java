package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2FloatMapSizeTester extends AbstractLong2FloatMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}