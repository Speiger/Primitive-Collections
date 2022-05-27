package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.AbstractObjectListTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectListRemoveElementsTester<T> extends AbstractObjectListTester<T>
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemoveElements() {
		getList().removeElements(0, 1);
		expectMissing(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_FromToLow() {
		try {
			getList().removeElements(-1, 1);
			fail("removeElements(toLow, high) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_toToHigh() {
		try {
			getList().removeElements(0, getNumElements()+1);
			fail("removeElements(low, toHigh) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_mixedUp() {
		try {
			getList().removeElements(1, 0);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
}