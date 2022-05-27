package speiger.src.testers.objects.tests.base;

import org.junit.Ignore;

import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractObjectListTester<T> extends AbstractObjectCollectionTester<T>
{
	protected final ObjectList<T> getList() {
		return (ObjectList<T>) collection;
	}

	@Override
	protected void expectContents(ObjectCollection<T> expectedCollection) {
		ObjectList<T> expectedList = ObjectHelpers.copyToList(expectedCollection);
		if (getList().size() != expectedList.size()) {
			fail("size mismatch: " + reportContext(expectedList));
		}
		for (int i = 0; i < expectedList.size(); i++) {
			T expected = expectedList.get(i);
			T actual = getList().get(i);
			if (!Objects.equals(expected, actual)) {
				fail("mismatch at index " + i + ": " + reportContext(expectedList));
			}
		}
	}

	private String reportContext(ObjectList<T> expected) {
		return String.format("expected collection %s; actual collection %s", expected, collection);
	}
}