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
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ShortMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2ShortMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2ShortMapAddToTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Long2ShortMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Short> {
	public static Long2ShortMapTestSuiteBuilder using(TestLong2ShortMapGenerator generator) {
		return (Long2ShortMapTestSuiteBuilder) new Long2ShortMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2ShortMapClearTester.class);
		testers.add(Long2ShortMapComputeTester.class);
		testers.add(Long2ShortMapComputeIfAbsentTester.class);
		testers.add(Long2ShortMapComputeIfPresentTester.class);
		testers.add(Long2ShortMapSupplyIfAbsentTester.class);
		testers.add(Long2ShortMapContainsKeyTester.class);
		testers.add(Long2ShortMapContainsValueTester.class);
		testers.add(Long2ShortMapEntrySetTester.class);
		testers.add(Long2ShortMapEqualsTester.class);
		testers.add(Long2ShortMapForEachTester.class);
		testers.add(Long2ShortMapGetTester.class);
		testers.add(Long2ShortMapGetOrDefaultTester.class);
		testers.add(Long2ShortMapHashCodeTester.class);
		testers.add(Long2ShortMapIsEmptyTester.class);
		testers.add(Long2ShortMapMergeTester.class);
		testers.add(Long2ShortMapPutTester.class);
		testers.add(Long2ShortMapAddToTester.class);
		testers.add(Long2ShortMapPutAllTester.class);
		testers.add(Long2ShortMapPutAllArrayTester.class);
		testers.add(Long2ShortMapPutIfAbsentTester.class);
		testers.add(Long2ShortMapRemoveTester.class);
		testers.add(Long2ShortMapRemoveEntryTester.class);
		testers.add(Long2ShortMapRemoveOrDefaultTester.class);
		testers.add(Long2ShortMapReplaceTester.class);
		testers.add(Long2ShortMapReplaceAllTester.class);
		testers.add(Long2ShortMapReplaceEntryTester.class);
		testers.add(Long2ShortMapSizeTester.class);
		testers.add(Long2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2ShortMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2ShortMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2ShortMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2ShortMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
	}
	
	protected ShortCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestShortCollectionGenerator generator) {
		return ShortCollectionTestSuiteBuilder.using(generator);
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