package speiger.src.tests.booleans.collections;

import java.util.function.Function;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.testers.CollectionAddAllTester;
import com.google.common.collect.testing.testers.CollectionAddTester;
import com.google.common.collect.testing.testers.CollectionContainsAllTester;
import com.google.common.collect.testing.testers.CollectionContainsTester;
import com.google.common.collect.testing.testers.CollectionIteratorTester;
import com.google.common.collect.testing.testers.CollectionRemoveAllTester;
import com.google.common.collect.testing.testers.CollectionRemoveTester;
import com.google.common.collect.testing.testers.CollectionRetainAllTester;
import com.google.common.collect.testing.testers.ListAddAllAtIndexTester;
import com.google.common.collect.testing.testers.ListAddAtIndexTester;
import com.google.common.collect.testing.testers.ListEqualsTester;
import com.google.common.collect.testing.testers.ListIndexOfTester;
import com.google.common.collect.testing.testers.ListLastIndexOfTester;
import com.google.common.collect.testing.testers.ListRetainAllTester;
import com.google.common.collect.testing.testers.ListSubListTester;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.booleans.lists.ImmutableBooleanList;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanLinkedList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.builder.BooleanListTestSuiteBuilder;
import speiger.src.testers.booleans.impl.SimpleBooleanTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionContainsAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionContainsTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionIteratorTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionRemoveAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionRetainAllTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAllArrayAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAllAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListEqualsTester;
import speiger.src.testers.booleans.tests.list.BooleanListExtractElementsTester;
import speiger.src.testers.booleans.tests.list.BooleanListIndexOfTester;
import speiger.src.testers.booleans.tests.list.BooleanListLastIndexOfTester;
import speiger.src.testers.booleans.tests.list.BooleanListRemoveElementsTester;
import speiger.src.testers.booleans.tests.list.BooleanListRetainAllTester;
import speiger.src.testers.booleans.tests.list.BooleanListSubListTester;

@SuppressWarnings("javadoc")
public class BooleanListTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("BooleanLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		listSuite(suite, "BooleanArrayList", BooleanArrayList::new);
		listSuite(suite, "BooleanLinkedList", BooleanLinkedList::new);
		listImmutableSuite(suite, "ImmutableBooleanList", ImmutableBooleanList::new);
	}
	
	private static void listSuite(TestSuite suite, String name, Function<boolean[], BooleanList> factory) {
		TestSuite data = new TestSuite(name);
		data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory))
			.named(name+" [collection size: zero]").withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ZERO, SpecialFeature.COPYING).suppressing(getSurpression(CollectionSize.ZERO, false))
			.createTestSuite());
		data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory))
			.named(name+" [collection size: one]").withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.ONE, SpecialFeature.COPYING).suppressing(getSurpression(CollectionSize.ONE, false))
			.createTestSuite());
		data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory)).named(name)
			.named(name+" [collection size: several]").withFeatures(ListFeature.GENERAL_PURPOSE, CollectionSize.SEVERAL, SpecialFeature.COPYING).suppressing(getSurpression(CollectionSize.SEVERAL, false))
			.createTestSuite());
		suite.addTest(data);
	}
	
	private static void listImmutableSuite(TestSuite suite, String name, Function<boolean[], BooleanList> factory) {
		TestSuite data = new TestSuite(name);
		data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory))
			.named(name+" [collection size: zero]").withFeatures(CollectionSize.ZERO, SpecialFeature.COPYING).suppressing(getSurpression(CollectionSize.ZERO, true))
			.createTestSuite());
		data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory))
			.named(name+" [collection size: one]").withFeatures(CollectionSize.ONE, SpecialFeature.COPYING).suppressing(getSurpression(CollectionSize.ONE, true))
			.createTestSuite());
		data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory))
			.named(name+" [collection size: several]").withFeatures(CollectionSize.SEVERAL, SpecialFeature.COPYING).suppressing(getSurpression(CollectionSize.SEVERAL, true))
			.createTestSuite());
		suite.addTest(data);
	}
	
	private static List<Method> getSurpression(CollectionSize size, boolean immutable) {
		List<Method> list = new ArrayList<>();
		if(size == CollectionSize.ONE) {
			getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
			if(immutable) {
				getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
			}
		}
		else {
			getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
			getSurpession(list, CollectionContainsTester.class, "testContains_no");
			getSurpession(list, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
			getSurpession(list, CollectionRemoveAllTester.class, "testRemoveAll_nonePresent");
			getSurpession(list, CollectionRemoveTester.class, "testRemove_present", "testRemove_notPresent");
			getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty", "testRetainAll_containsDuplicatesSizeSeveral", "testRetainAll_partialOverlap");
			getSurpession(list, BooleanCollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
			getSurpession(list, BooleanCollectionContainsTester.class, "testContains_no");
			getSurpession(list, BooleanCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
			getSurpession(list, BooleanCollectionRemoveAllTester.class, "testRemoveAll_nonePresentFetchRemoved", "testRemoveAll_someFetchRemovedElements", "testRemoveAll_nonePresent");
			getSurpession(list, BooleanCollectionRetainAllTester.class, "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_partialOverlap", "testRetainAll_containsDuplicatesSizeSeveral", "testRetainAll_partialOverlap");
			getSurpession(list, ListAddAllAtIndexTester.class, "testAddAllAtIndex_negative", "testAddAllAtIndex_tooLarge");
			getSurpession(list, ListAddAtIndexTester.class, "testAddAtIndex_tooLarge", "testAddAtIndex_negative");
			getSurpession(list, ListEqualsTester.class, "testEquals_otherListWithDifferentElements");
			getSurpession(list, ListIndexOfTester.class, "testFind_no");
			getSurpession(list, ListLastIndexOfTester.class, "testLastIndexOf_duplicate", "testFind_no", "testFind_yes");
			getSurpession(list, ListRetainAllTester.class, "testRetainAll_duplicatesRemoved", "testRetainAll_countIgnored");
			getSurpession(list, ListSubListTester.class, "testSubList_lastIndexOf", "testSubList_contains", "testSubList_indexOf");
			getSurpession(list, BooleanListAddAllAtIndexTester.class, "testAddAllAtIndex_negative", "testAddAllAtIndex_tooLarge");
			getSurpession(list, BooleanListAddAllArrayAtIndexTester.class, "testAddAllArrayAtIndex_tooLarge", "testAddAllArrayAtIndex_negative");
			getSurpession(list, BooleanListAddAtIndexTester.class, "testAddAtIndex_tooLarge", "testAddAtIndex_negative");
			getSurpession(list, BooleanListEqualsTester.class, "testEquals_otherListWithDifferentElements");
			getSurpession(list, BooleanListExtractElementsTester.class, "testRemoveElements");
			getSurpession(list, BooleanListIndexOfTester.class, "testFind_no");
			getSurpession(list, BooleanListLastIndexOfTester.class, "testLastIndexOf_duplicate", "testFind_no", "testFind_yes");
			getSurpession(list, BooleanListRemoveElementsTester.class, "testRemoveElements");
			getSurpession(list, BooleanListRetainAllTester.class, "testRetainAll_duplicatesRemoved", "testRetainAll_countIgnored");
			getSurpession(list, BooleanListSubListTester.class, "testSubList_lastIndexOf", "testSubList_contains", "testSubList_indexOf");
			if(immutable) {
				getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
				getSurpession(list, CollectionRemoveTester.class, "testRemove_unsupportedNotPresent");
				getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
				getSurpession(list, BooleanCollectionAddTester.class, "testAdd_unsupportedNotPresent");
				getSurpession(list, ListAddAllAtIndexTester.class, "testAddAllAtIndex_unsupportedSomePresent");
				getSurpession(list, ListAddAtIndexTester.class, "testAddAtIndex_unsupportedNotPresent");
				getSurpession(list, BooleanListAddAllAtIndexTester.class, "testAddAllAtIndex_unsupportedSomePresent");
				getSurpession(list, BooleanListAddAllArrayAtIndexTester.class, "testAddAllArrayAtIndex_unsupportedSomePresent");
				getSurpession(list, BooleanListAddAtIndexTester.class, "testAddAtIndex_unsupportedNotPresent");
			}
		}
		return list;
	}
	
	private static void getSurpession(List<Method> methods, Class<?> clz, String...args) {
		Set<String> set = new HashSet<>(Arrays.asList(args));
		try {
			for(Method method : clz.getMethods()) {
				if(set.contains(method.getName())) methods.add(method);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}