package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ByteMapSizeTester<T> extends AbstractObject2ByteMapTester<T> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}