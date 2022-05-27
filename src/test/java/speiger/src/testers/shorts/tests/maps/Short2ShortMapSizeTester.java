package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ShortMapTester;

@Ignore
public class Short2ShortMapSizeTester extends AbstractShort2ShortMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}