package speiger.src.testers.chars.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.chars.tests.collection.CharCollectionAddAllArrayTester;
import speiger.src.testers.chars.tests.collection.CharCollectionAddAllTester;
import speiger.src.testers.chars.tests.collection.CharCollectionAddTester;
import speiger.src.testers.chars.tests.collection.CharCollectionClearTester;
import speiger.src.testers.chars.tests.collection.CharCollectionContainsAllTester;
import speiger.src.testers.chars.tests.collection.CharCollectionContainsAnyTester;
import speiger.src.testers.chars.tests.collection.CharCollectionContainsTester;
import speiger.src.testers.chars.tests.collection.CharCollectionCopyTester;
import speiger.src.testers.chars.tests.collection.CharCollectionEqualsTester;
import speiger.src.testers.chars.tests.collection.CharCollectionForEachTester;
import speiger.src.testers.chars.tests.collection.CharCollectionIteratorTester;
import speiger.src.testers.chars.tests.collection.CharCollectionRemoveAllTester;
import speiger.src.testers.chars.tests.collection.CharCollectionRemoveIfTester;
import speiger.src.testers.chars.tests.collection.CharCollectionStreamTester;
import speiger.src.testers.chars.tests.collection.CharCollectionRetainAllTester;
import speiger.src.testers.chars.tests.collection.CharCollectionToArrayTester;

public class CharCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Character> {
	public static CharCollectionTestSuiteBuilder using(TestCharCollectionGenerator generator) {
		return (CharCollectionTestSuiteBuilder) new CharCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(CharCollectionAddAllTester.class);
		testers.add(CharCollectionAddAllArrayTester.class);
		testers.add(CharCollectionAddTester.class);
		testers.add(CharCollectionClearTester.class);
		testers.add(CharCollectionContainsAllTester.class);
		testers.add(CharCollectionContainsAnyTester.class);
		testers.add(CharCollectionContainsTester.class);
		testers.add(CharCollectionCopyTester.class);
		testers.add(CharCollectionEqualsTester.class);
		testers.add(CharCollectionForEachTester.class);
		testers.add(CharCollectionIteratorTester.class);
		testers.add(CharCollectionRemoveAllTester.class);
		testers.add(CharCollectionRetainAllTester.class);
		testers.add(CharCollectionRemoveIfTester.class);
		testers.add(CharCollectionStreamTester.class);
		testers.add(CharCollectionToArrayTester.class);
		return testers;
	}
}