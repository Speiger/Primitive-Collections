package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2FloatMapTester;

@Ignore
public class Short2FloatMapSizeTester extends AbstractShort2FloatMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}