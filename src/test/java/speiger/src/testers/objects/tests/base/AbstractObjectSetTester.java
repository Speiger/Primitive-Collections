package speiger.src.testers.objects.tests.base;

import org.junit.Ignore;

import speiger.src.collections.objects.sets.ObjectSet;

@Ignore
public class AbstractObjectSetTester<T> extends AbstractObjectCollectionTester<T>
{
	protected final ObjectSet<T> getSet() {
		return (ObjectSet<T>)collection;
	}
}