package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2LongMapSizeTester extends AbstractLong2LongMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}