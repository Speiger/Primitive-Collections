package speiger.src.tests.chars.collections;

import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.lists.ImmutableCharList;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharLinkedList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.builder.CharListTestSuiteBuilder;
import speiger.src.testers.chars.impl.SimpleCharTestGenerator;

public class CharListTests extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("CharLists");
		suite(suite);
		return suite;
	}
	
	public static void suite(TestSuite suite)
	{
		suite.addTest(listSuite("CharArrayList", CharArrayList::new));
		suite.addTest(listSuite("CharLinkedList", CharLinkedList::new));
		suite.addTest(listImmutableSuite("ImmutableCharList", ImmutableCharList::new));
	}
	
	private static Test listSuite(String name, Function<char[], CharList> factory) {
		return CharListTestSuiteBuilder.using(new SimpleCharTestGenerator.Lists(factory)).named(name)
				.withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ANY)
				.createTestSuite();
	}
	
	private static Test listImmutableSuite(String name, Function<char[], CharList> factory) {
		return CharListTestSuiteBuilder.using(new SimpleCharTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY)
				.createTestSuite();
	}
}