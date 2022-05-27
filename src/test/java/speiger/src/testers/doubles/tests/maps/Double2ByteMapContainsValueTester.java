package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2ByteMapContainsValueTester extends AbstractDouble2ByteMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsValue(present) should return true", getMap().containsValue(v0()));
	}

	public void testContains_no() {
		assertFalse("containsValue(notPresent) should return false", getMap().containsValue(v3()));
	}
}