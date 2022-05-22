package speiger.src.testers.doubles.tests.base;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.utils.DoubleHelpers;

public class AbstractDoubleListTester extends AbstractDoubleCollectionTester {
	protected final DoubleList getList() {
		return (DoubleList) collection;
	}

	@Override
	protected void expectContents(DoubleCollection expectedCollection) {
		DoubleList expectedList = DoubleHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			double expected = expectedList.getDouble(i);
			double actual = getList().getDouble(i);
			if (Double.doubleToLongBits(expected) != Double.doubleToLongBits(actual)) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(DoubleList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}