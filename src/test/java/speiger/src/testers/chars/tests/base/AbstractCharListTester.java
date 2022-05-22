package speiger.src.testers.chars.tests.base;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.utils.CharHelpers;

public class AbstractCharListTester extends AbstractCharCollectionTester {
	protected final CharList getList() {
		return (CharList) collection;
	}

	@Override
	protected void expectContents(CharCollection expectedCollection) {
		CharList expectedList = CharHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			char expected = expectedList.getChar(i);
			char actual = getList().getChar(i);
			if (expected != actual) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(CharList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}