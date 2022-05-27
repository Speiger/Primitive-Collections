package speiger.src.tests.ints.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.lists.ImmutableIntList;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntLinkedList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.builder.IntListTestSuiteBuilder;
import speiger.src.testers.ints.impl.SimpleIntTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

public class IntListTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("IntLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("IntArrayList", IntArrayList::new));
		suite.addTest(listSuite("IntLinkedList", IntLinkedList::new));
		suite.addTest(listImmutableSuite("ImmutableIntList", ImmutableIntList::new));
	}
	
	private static Test listSuite(String name, Function<int[], IntList> factory) {
		return IntListTestSuiteBuilder.using(new SimpleIntTestGenerator.Lists(factory)).named(name)
				.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
	private static Test listImmutableSuite(String name, Function<int[], IntList> factory) {
		return IntListTestSuiteBuilder.using(new SimpleIntTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
}