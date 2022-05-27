package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2DoubleMapSizeTester<T> extends AbstractObject2DoubleMapTester<T> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}