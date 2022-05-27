package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2BooleanMapSizeTester<T> extends AbstractObject2BooleanMapTester<T> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}