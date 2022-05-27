package speiger.src.testers.chars.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ONE;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.chars.tests.base.AbstractCharListTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharListPresentTester extends AbstractCharListTester
{
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void testIfAbsent() {
		assertFalse("addIfPresent(absent) should return false", getList().addIfPresent(e1()));
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void testIfPresent() {
		assertTrue("addIfPresent(present) should return true", getList().addIfPresent(e0()));
		expectAdded(e0());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void test_IfAbsentUnsupported() {
		try {
			assertFalse("addIfPresent(absent) should return false", getList().addIfPresent(e1()));			
		} catch (UnsupportedOperationException e) {
			//Success
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	@CollectionSize.Require(ONE)
	public void test_IfPresentUnsupported() {
		try {
			assertFalse("addIfPresent(present) should return false", getList().addIfPresent(e0()));			
		} catch (UnsupportedOperationException e) {
			//Success
		}
	}
}