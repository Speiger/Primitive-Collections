package speiger.src.testers.chars.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2IntMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2IntMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2IntMapAddToTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Char2IntMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Integer> {
	public static Char2IntMapTestSuiteBuilder using(TestChar2IntMapGenerator generator) {
		return (Char2IntMapTestSuiteBuilder) new Char2IntMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2IntMapClearTester.class);
		testers.add(Char2IntMapComputeTester.class);
		testers.add(Char2IntMapComputeIfAbsentTester.class);
		testers.add(Char2IntMapComputeIfPresentTester.class);
		testers.add(Char2IntMapSupplyIfAbsentTester.class);
		testers.add(Char2IntMapContainsKeyTester.class);
		testers.add(Char2IntMapContainsValueTester.class);
		testers.add(Char2IntMapEntrySetTester.class);
		testers.add(Char2IntMapEqualsTester.class);
		testers.add(Char2IntMapForEachTester.class);
		testers.add(Char2IntMapGetTester.class);
		testers.add(Char2IntMapGetOrDefaultTester.class);
		testers.add(Char2IntMapHashCodeTester.class);
		testers.add(Char2IntMapIsEmptyTester.class);
		testers.add(Char2IntMapMergeTester.class);
		testers.add(Char2IntMapPutTester.class);
		testers.add(Char2IntMapAddToTester.class);
		testers.add(Char2IntMapPutAllTester.class);
		testers.add(Char2IntMapPutAllArrayTester.class);
		testers.add(Char2IntMapPutIfAbsentTester.class);
		testers.add(Char2IntMapRemoveTester.class);
		testers.add(Char2IntMapRemoveEntryTester.class);
		testers.add(Char2IntMapRemoveOrDefaultTester.class);
		testers.add(Char2IntMapReplaceTester.class);
		testers.add(Char2IntMapReplaceAllTester.class);
		testers.add(Char2IntMapReplaceEntryTester.class);
		testers.add(Char2IntMapSizeTester.class);
		testers.add(Char2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Integer>, Map.Entry<Character, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2IntMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2IntMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2IntMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2IntMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
	}
	
	protected IntCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestIntCollectionGenerator generator) {
		return IntCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Feature<?>> computeValuesCollectionFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> valuesCollectionFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUE_QUERIES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		}
		valuesCollectionFeatures.remove(SpecialFeature.COPYING);
		return valuesCollectionFeatures;
	}
	
	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}