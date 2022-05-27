package speiger.src.testers.floats.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2LongMapSizeTester extends AbstractFloat2LongMapTester {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}