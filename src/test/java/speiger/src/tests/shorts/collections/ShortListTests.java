package speiger.src.tests.shorts.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.lists.ImmutableShortList;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortLinkedList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.builder.ShortListTestSuiteBuilder;
import speiger.src.testers.shorts.impl.SimpleShortTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class ShortListTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("ShortLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("ShortArrayList", ShortArrayList::new));
		suite.addTest(listSuite("ShortLinkedList", ShortLinkedList::new));
		suite.addTest(listImmutableSuite("ImmutableShortList", ImmutableShortList::new));
	}
	
	private static Test listSuite(String name, Function<short[], ShortList> factory) {
		return ShortListTestSuiteBuilder.using(new SimpleShortTestGenerator.Lists(factory)).named(name)
				.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
	private static Test listImmutableSuite(String name, Function<short[], ShortList> factory) {
		return ShortListTestSuiteBuilder.using(new SimpleShortTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY, SpecialFeature.COPYING)
				.createTestSuite();
	}
	
}