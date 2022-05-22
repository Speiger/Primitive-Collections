package speiger.src.testers.longs.tests.base;

import org.junit.Ignore;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
public class AbstractLongListTester extends AbstractLongCollectionTester {
	protected final LongList getList() {
		return (LongList) collection;
	}

	@Override
	protected void expectContents(LongCollection expectedCollection) {
		LongList expectedList = LongHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			long expected = expectedList.getLong(i);
			long actual = getList().getLong(i);
			if (expected != actual) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(LongList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}