package speiger.src.testers.bytes.tests.base;

import org.junit.Ignore;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.utils.ByteHelpers;

@Ignore
public class AbstractByteListTester extends AbstractByteCollectionTester {
	protected final ByteList getList() {
		return (ByteList) collection;
	}

	@Override
	protected void expectContents(ByteCollection expectedCollection) {
		ByteList expectedList = ByteHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			byte expected = expectedList.getByte(i);
			byte actual = getList().getByte(i);
			if (expected != actual) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(ByteList expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}