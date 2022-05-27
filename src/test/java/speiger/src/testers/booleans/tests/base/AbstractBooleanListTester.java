package speiger.src.testers.booleans.tests.base;

import org.junit.Ignore;


import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.utils.BooleanHelpers;

@Ignore
public class AbstractBooleanListTester extends AbstractBooleanCollectionTester
{
	protected final BooleanList getList() {
		return (BooleanList) collection;
	}

	@Override
	protected void expectContents(BooleanCollection expectedCollection) {
		BooleanList expectedList = BooleanHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			boolean expected = expectedList.getBoolean(i);
			boolean actual = getList().getBoolean(i);
			if (expected != actual) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(BooleanList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}