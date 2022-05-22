package speiger.src.testers.shorts.tests.base;

import org.junit.Ignore;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.utils.ShortHelpers;

@Ignore
public class AbstractShortListTester extends AbstractShortCollectionTester {
	protected final ShortList getList() {
		return (ShortList) collection;
	}

	@Override
	protected void expectContents(ShortCollection expectedCollection) {
		ShortList expectedList = ShortHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			short expected = expectedList.getShort(i);
			short actual = getList().getShort(i);
			if (expected != actual) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(ShortList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}