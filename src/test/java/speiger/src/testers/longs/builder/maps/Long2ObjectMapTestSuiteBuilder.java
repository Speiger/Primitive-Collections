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
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.testers.objects.builder.ObjectCollectionTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ObjectMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2ObjectMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ObjectMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Long2ObjectMapTestSuiteBuilder<V> extends MapTestSuiteBuilder<Long, V> {
	public static <V> Long2ObjectMapTestSuiteBuilder<V> using(TestLong2ObjectMapGenerator<V> generator) {
		return (Long2ObjectMapTestSuiteBuilder<V>) new Long2ObjectMapTestSuiteBuilder<V>().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2ObjectMapClearTester.class);
		testers.add(Long2ObjectMapComputeTester.class);
		testers.add(Long2ObjectMapComputeIfAbsentTester.class);
		testers.add(Long2ObjectMapComputeIfPresentTester.class);
		testers.add(Long2ObjectMapSupplyIfAbsentTester.class);
		testers.add(Long2ObjectMapContainsKeyTester.class);
		testers.add(Long2ObjectMapContainsValueTester.class);
		testers.add(Long2ObjectMapEntrySetTester.class);
		testers.add(Long2ObjectMapEqualsTester.class);
		testers.add(Long2ObjectMapForEachTester.class);
		testers.add(Long2ObjectMapGetTester.class);
		testers.add(Long2ObjectMapGetOrDefaultTester.class);
		testers.add(Long2ObjectMapHashCodeTester.class);
		testers.add(Long2ObjectMapIsEmptyTester.class);
		testers.add(Long2ObjectMapMergeTester.class);
		testers.add(Long2ObjectMapPutTester.class);
		testers.add(Long2ObjectMapPutAllTester.class);
		testers.add(Long2ObjectMapPutAllArrayTester.class);
		testers.add(Long2ObjectMapPutIfAbsentTester.class);
		testers.add(Long2ObjectMapRemoveTester.class);
		testers.add(Long2ObjectMapRemoveEntryTester.class);
		testers.add(Long2ObjectMapRemoveOrDefaultTester.class);
		testers.add(Long2ObjectMapReplaceTester.class);
		testers.add(Long2ObjectMapReplaceAllTester.class);
		testers.add(Long2ObjectMapReplaceEntryTester.class);
		testers.add(Long2ObjectMapSizeTester.class);
		testers.add(Long2ObjectMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, V>, Map.Entry<Long, V>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2ObjectMapGenerators.MapEntrySetGenerator<>(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2ObjectMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2ObjectMapGenerators.MapValueCollectionGenerator<>(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2ObjectMap.Entry<V>> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2ObjectMap.Entry<V>> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
	}
	
	protected ObjectCollectionTestSuiteBuilder<V> createDerivedValueCollectionSuite(TestObjectCollectionGenerator<V> generator) {
		return ObjectCollectionTestSuiteBuilder.using(generator);
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