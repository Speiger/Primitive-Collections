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
import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2DoubleMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2DoubleMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapAddToTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Long2DoubleMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Double> {
	public static Long2DoubleMapTestSuiteBuilder using(TestLong2DoubleMapGenerator generator) {
		return (Long2DoubleMapTestSuiteBuilder) new Long2DoubleMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2DoubleMapClearTester.class);
		testers.add(Long2DoubleMapComputeTester.class);
		testers.add(Long2DoubleMapComputeIfAbsentTester.class);
		testers.add(Long2DoubleMapComputeIfPresentTester.class);
		testers.add(Long2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Long2DoubleMapContainsKeyTester.class);
		testers.add(Long2DoubleMapContainsValueTester.class);
		testers.add(Long2DoubleMapEntrySetTester.class);
		testers.add(Long2DoubleMapEqualsTester.class);
		testers.add(Long2DoubleMapForEachTester.class);
		testers.add(Long2DoubleMapGetTester.class);
		testers.add(Long2DoubleMapGetOrDefaultTester.class);
		testers.add(Long2DoubleMapHashCodeTester.class);
		testers.add(Long2DoubleMapIsEmptyTester.class);
		testers.add(Long2DoubleMapMergeTester.class);
		testers.add(Long2DoubleMapPutTester.class);
		testers.add(Long2DoubleMapAddToTester.class);
		testers.add(Long2DoubleMapPutAllTester.class);
		testers.add(Long2DoubleMapPutAllArrayTester.class);
		testers.add(Long2DoubleMapPutIfAbsentTester.class);
		testers.add(Long2DoubleMapRemoveTester.class);
		testers.add(Long2DoubleMapRemoveEntryTester.class);
		testers.add(Long2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Long2DoubleMapReplaceTester.class);
		testers.add(Long2DoubleMapReplaceAllTester.class);
		testers.add(Long2DoubleMapReplaceEntryTester.class);
		testers.add(Long2DoubleMapSizeTester.class);
		testers.add(Long2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Double>, Map.Entry<Long, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2DoubleMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2DoubleMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2DoubleMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2DoubleMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
	}
	
	protected DoubleCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestDoubleCollectionGenerator generator) {
		return DoubleCollectionTestSuiteBuilder.using(generator);
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