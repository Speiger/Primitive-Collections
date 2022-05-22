package speiger.src.testers.ints.tests.base;

import org.junit.Ignore;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.utils.IntHelpers;

@Ignore
public class AbstractIntListTester extends AbstractIntCollectionTester {
	protected final IntList getList() {
		return (IntList) collection;
	}

	@Override
	protected void expectContents(IntCollection expectedCollection) {
		IntList expectedList = IntHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			int expected = expectedList.getInt(i);
			int actual = getList().getInt(i);
			if (expected != actual) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(IntList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}