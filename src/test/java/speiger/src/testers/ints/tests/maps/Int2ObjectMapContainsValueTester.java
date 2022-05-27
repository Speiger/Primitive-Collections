package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2ObjectMapContainsValueTester<V> extends AbstractInt2ObjectMapTester<V>
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsValue(present) should return true", getMap().containsValue(v0()));
	}

	public void testContains_no() {
		assertFalse("containsValue(notPresent) should return false", getMap().containsValue(v3()));
	}
}