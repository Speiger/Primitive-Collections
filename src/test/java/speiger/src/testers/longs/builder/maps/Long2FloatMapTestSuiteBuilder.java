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
import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2FloatMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2FloatMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2FloatMapAddToTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Long2FloatMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Float> {
	public static Long2FloatMapTestSuiteBuilder using(TestLong2FloatMapGenerator generator) {
		return (Long2FloatMapTestSuiteBuilder) new Long2FloatMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2FloatMapClearTester.class);
		testers.add(Long2FloatMapComputeTester.class);
		testers.add(Long2FloatMapComputeIfAbsentTester.class);
		testers.add(Long2FloatMapComputeIfPresentTester.class);
		testers.add(Long2FloatMapSupplyIfAbsentTester.class);
		testers.add(Long2FloatMapContainsKeyTester.class);
		testers.add(Long2FloatMapContainsValueTester.class);
		testers.add(Long2FloatMapEntrySetTester.class);
		testers.add(Long2FloatMapEqualsTester.class);
		testers.add(Long2FloatMapForEachTester.class);
		testers.add(Long2FloatMapGetTester.class);
		testers.add(Long2FloatMapGetOrDefaultTester.class);
		testers.add(Long2FloatMapHashCodeTester.class);
		testers.add(Long2FloatMapIsEmptyTester.class);
		testers.add(Long2FloatMapMergeTester.class);
		testers.add(Long2FloatMapPutTester.class);
		testers.add(Long2FloatMapAddToTester.class);
		testers.add(Long2FloatMapPutAllTester.class);
		testers.add(Long2FloatMapPutAllArrayTester.class);
		testers.add(Long2FloatMapPutIfAbsentTester.class);
		testers.add(Long2FloatMapRemoveTester.class);
		testers.add(Long2FloatMapRemoveEntryTester.class);
		testers.add(Long2FloatMapRemoveOrDefaultTester.class);
		testers.add(Long2FloatMapReplaceTester.class);
		testers.add(Long2FloatMapReplaceAllTester.class);
		testers.add(Long2FloatMapReplaceEntryTester.class);
		testers.add(Long2FloatMapSizeTester.class);
		testers.add(Long2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Float>, Map.Entry<Long, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2FloatMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2FloatMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2FloatMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2FloatMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
	}
	
	protected FloatCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestFloatCollectionGenerator generator) {
		return FloatCollectionTestSuiteBuilder.using(generator);
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