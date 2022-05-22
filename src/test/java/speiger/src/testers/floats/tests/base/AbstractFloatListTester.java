package speiger.src.testers.floats.tests.base;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.utils.FloatHelpers;

public class AbstractFloatListTester extends AbstractFloatCollectionTester {
	protected final FloatList getList() {
		return (FloatList) collection;
	}

	@Override
	protected void expectContents(FloatCollection expectedCollection) {
		FloatList expectedList = FloatHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			float expected = expectedList.getFloat(i);
			float actual = getList().getFloat(i);
			if (Float.floatToIntBits(expected) != Float.floatToIntBits(actual)) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(FloatList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}