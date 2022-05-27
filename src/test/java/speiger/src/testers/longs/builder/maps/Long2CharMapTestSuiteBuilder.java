package speiger.src.testers.longs.builder.maps;

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
import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2CharMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2CharMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2CharMapAddToTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Long2CharMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Character> {
	public static Long2CharMapTestSuiteBuilder using(TestLong2CharMapGenerator generator) {
		return (Long2CharMapTestSuiteBuilder) new Long2CharMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2CharMapClearTester.class);
		testers.add(Long2CharMapComputeTester.class);
		testers.add(Long2CharMapComputeIfAbsentTester.class);
		testers.add(Long2CharMapComputeIfPresentTester.class);
		testers.add(Long2CharMapSupplyIfAbsentTester.class);
		testers.add(Long2CharMapContainsKeyTester.class);
		testers.add(Long2CharMapContainsValueTester.class);
		testers.add(Long2CharMapEntrySetTester.class);
		testers.add(Long2CharMapEqualsTester.class);
		testers.add(Long2CharMapForEachTester.class);
		testers.add(Long2CharMapGetTester.class);
		testers.add(Long2CharMapGetOrDefaultTester.class);
		testers.add(Long2CharMapHashCodeTester.class);
		testers.add(Long2CharMapIsEmptyTester.class);
		testers.add(Long2CharMapMergeTester.class);
		testers.add(Long2CharMapPutTester.class);
		testers.add(Long2CharMapAddToTester.class);
		testers.add(Long2CharMapPutAllTester.class);
		testers.add(Long2CharMapPutAllArrayTester.class);
		testers.add(Long2CharMapPutIfAbsentTester.class);
		testers.add(Long2CharMapRemoveTester.class);
		testers.add(Long2CharMapRemoveEntryTester.class);
		testers.add(Long2CharMapRemoveOrDefaultTester.class);
		testers.add(Long2CharMapReplaceTester.class);
		testers.add(Long2CharMapReplaceAllTester.class);
		testers.add(Long2CharMapReplaceEntryTester.class);
		testers.add(Long2CharMapSizeTester.class);
		testers.add(Long2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Character>, Map.Entry<Long, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2CharMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2CharMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2CharMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2CharMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
	}
	
	protected CharCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestCharCollectionGenerator generator) {
		return CharCollectionTestSuiteBuilder.using(generator);
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