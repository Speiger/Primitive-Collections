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
import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2IntMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2IntMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2IntMapAddToTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Long2IntMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Integer> {
	public static Long2IntMapTestSuiteBuilder using(TestLong2IntMapGenerator generator) {
		return (Long2IntMapTestSuiteBuilder) new Long2IntMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2IntMapClearTester.class);
		testers.add(Long2IntMapComputeTester.class);
		testers.add(Long2IntMapComputeIfAbsentTester.class);
		testers.add(Long2IntMapComputeIfPresentTester.class);
		testers.add(Long2IntMapSupplyIfAbsentTester.class);
		testers.add(Long2IntMapContainsKeyTester.class);
		testers.add(Long2IntMapContainsValueTester.class);
		testers.add(Long2IntMapEntrySetTester.class);
		testers.add(Long2IntMapEqualsTester.class);
		testers.add(Long2IntMapForEachTester.class);
		testers.add(Long2IntMapGetTester.class);
		testers.add(Long2IntMapGetOrDefaultTester.class);
		testers.add(Long2IntMapHashCodeTester.class);
		testers.add(Long2IntMapIsEmptyTester.class);
		testers.add(Long2IntMapMergeTester.class);
		testers.add(Long2IntMapPutTester.class);
		testers.add(Long2IntMapAddToTester.class);
		testers.add(Long2IntMapPutAllTester.class);
		testers.add(Long2IntMapPutAllArrayTester.class);
		testers.add(Long2IntMapPutIfAbsentTester.class);
		testers.add(Long2IntMapRemoveTester.class);
		testers.add(Long2IntMapRemoveEntryTester.class);
		testers.add(Long2IntMapRemoveOrDefaultTester.class);
		testers.add(Long2IntMapReplaceTester.class);
		testers.add(Long2IntMapReplaceAllTester.class);
		testers.add(Long2IntMapReplaceEntryTester.class);
		testers.add(Long2IntMapSizeTester.class);
		testers.add(Long2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Integer>, Map.Entry<Long, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2IntMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2IntMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2IntMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2IntMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
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