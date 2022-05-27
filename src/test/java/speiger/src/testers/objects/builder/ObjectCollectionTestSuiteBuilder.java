package speiger.src.testers.objects.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionAddAllArrayTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionAddAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionAddTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionClearTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionContainsAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionContainsAnyTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionContainsTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionCopyTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionEqualsTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionForEachTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveIfTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionToArrayTester;

@SuppressWarnings("javadoc")
public class ObjectCollectionTestSuiteBuilder<T> extends CollectionTestSuiteBuilder<T> {
	public static <T> ObjectCollectionTestSuiteBuilder<T> using(TestObjectCollectionGenerator<T> generator) {
		return (ObjectCollectionTestSuiteBuilder<T>) new ObjectCollectionTestSuiteBuilder<T>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(ObjectCollectionAddAllTester.class);
		testers.add(ObjectCollectionAddAllArrayTester.class);
		testers.add(ObjectCollectionAddTester.class);
		testers.add(ObjectCollectionClearTester.class);
		testers.add(ObjectCollectionContainsAllTester.class);
		testers.add(ObjectCollectionContainsAnyTester.class);
		testers.add(ObjectCollectionContainsTester.class);
		testers.add(ObjectCollectionCopyTester.class);
		testers.add(ObjectCollectionEqualsTester.class);
		testers.add(ObjectCollectionForEachTester.class);
		testers.add(ObjectCollectionIteratorTester.class);
		testers.add(ObjectCollectionRemoveAllTester.class);
		testers.add(ObjectCollectionRetainAllTester.class);
		testers.add(ObjectCollectionRemoveIfTester.class);
		testers.add(ObjectCollectionToArrayTester.class);
		return testers;
	}
}