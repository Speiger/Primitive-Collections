package speiger.src.testers.longs.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ByteMapSizeTester extends AbstractLong2ByteMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}