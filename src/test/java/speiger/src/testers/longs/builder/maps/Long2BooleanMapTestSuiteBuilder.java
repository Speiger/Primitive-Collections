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
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2BooleanMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2BooleanMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Long2BooleanMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Boolean> {
	public static Long2BooleanMapTestSuiteBuilder using(TestLong2BooleanMapGenerator generator) {
		return (Long2BooleanMapTestSuiteBuilder) new Long2BooleanMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2BooleanMapClearTester.class);
		testers.add(Long2BooleanMapComputeTester.class);
		testers.add(Long2BooleanMapComputeIfAbsentTester.class);
		testers.add(Long2BooleanMapComputeIfPresentTester.class);
		testers.add(Long2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Long2BooleanMapContainsKeyTester.class);
		testers.add(Long2BooleanMapContainsValueTester.class);
		testers.add(Long2BooleanMapEntrySetTester.class);
		testers.add(Long2BooleanMapEqualsTester.class);
		testers.add(Long2BooleanMapForEachTester.class);
		testers.add(Long2BooleanMapGetTester.class);
		testers.add(Long2BooleanMapGetOrDefaultTester.class);
		testers.add(Long2BooleanMapHashCodeTester.class);
		testers.add(Long2BooleanMapIsEmptyTester.class);
		testers.add(Long2BooleanMapMergeTester.class);
		testers.add(Long2BooleanMapPutTester.class);
		testers.add(Long2BooleanMapPutAllTester.class);
		testers.add(Long2BooleanMapPutAllArrayTester.class);
		testers.add(Long2BooleanMapPutIfAbsentTester.class);
		testers.add(Long2BooleanMapRemoveTester.class);
		testers.add(Long2BooleanMapRemoveEntryTester.class);
		testers.add(Long2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Long2BooleanMapReplaceTester.class);
		testers.add(Long2BooleanMapReplaceAllTester.class);
		testers.add(Long2BooleanMapReplaceEntryTester.class);
		testers.add(Long2BooleanMapSizeTester.class);
		testers.add(Long2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Boolean>, Map.Entry<Long, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2BooleanMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2BooleanMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2BooleanMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
	}
	
	protected BooleanCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestBooleanCollectionGenerator generator) {
		return BooleanCollectionTestSuiteBuilder.using(generator);
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

	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}