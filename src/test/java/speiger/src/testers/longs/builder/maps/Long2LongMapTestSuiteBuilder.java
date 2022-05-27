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
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2LongMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2LongMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2LongMapAddToTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Long2LongMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Long> {
	public static Long2LongMapTestSuiteBuilder using(TestLong2LongMapGenerator generator) {
		return (Long2LongMapTestSuiteBuilder) new Long2LongMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2LongMapClearTester.class);
		testers.add(Long2LongMapComputeTester.class);
		testers.add(Long2LongMapComputeIfAbsentTester.class);
		testers.add(Long2LongMapComputeIfPresentTester.class);
		testers.add(Long2LongMapSupplyIfAbsentTester.class);
		testers.add(Long2LongMapContainsKeyTester.class);
		testers.add(Long2LongMapContainsValueTester.class);
		testers.add(Long2LongMapEntrySetTester.class);
		testers.add(Long2LongMapEqualsTester.class);
		testers.add(Long2LongMapForEachTester.class);
		testers.add(Long2LongMapGetTester.class);
		testers.add(Long2LongMapGetOrDefaultTester.class);
		testers.add(Long2LongMapHashCodeTester.class);
		testers.add(Long2LongMapIsEmptyTester.class);
		testers.add(Long2LongMapMergeTester.class);
		testers.add(Long2LongMapPutTester.class);
		testers.add(Long2LongMapAddToTester.class);
		testers.add(Long2LongMapPutAllTester.class);
		testers.add(Long2LongMapPutAllArrayTester.class);
		testers.add(Long2LongMapPutIfAbsentTester.class);
		testers.add(Long2LongMapRemoveTester.class);
		testers.add(Long2LongMapRemoveEntryTester.class);
		testers.add(Long2LongMapRemoveOrDefaultTester.class);
		testers.add(Long2LongMapReplaceTester.class);
		testers.add(Long2LongMapReplaceAllTester.class);
		testers.add(Long2LongMapReplaceEntryTester.class);
		testers.add(Long2LongMapSizeTester.class);
		testers.add(Long2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Long>, Map.Entry<Long, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2LongMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2LongMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2LongMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2LongMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
	}
	
	protected LongCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestLongCollectionGenerator generator) {
		return LongCollectionTestSuiteBuilder.using(generator);
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