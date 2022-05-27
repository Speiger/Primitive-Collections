package speiger.src.testers.longs.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.AbstractLongSetTester;

@Ignore
@SuppressWarnings("javadoc")
public class LongSetRemoveTester extends AbstractLongSetTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_present() {
		getSet().remove(e0());
		assertFalse("After remove(present) a set should not contain the removed element.", getSet().contains(e0()));
	}
}