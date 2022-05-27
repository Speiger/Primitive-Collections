package speiger.src.testers.ints.builder.maps;

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
import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2LongMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2LongMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2LongMapAddToTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Int2LongMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Long> {
	public static Int2LongMapTestSuiteBuilder using(TestInt2LongMapGenerator generator) {
		return (Int2LongMapTestSuiteBuilder) new Int2LongMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2LongMapClearTester.class);
		testers.add(Int2LongMapComputeTester.class);
		testers.add(Int2LongMapComputeIfAbsentTester.class);
		testers.add(Int2LongMapComputeIfPresentTester.class);
		testers.add(Int2LongMapSupplyIfAbsentTester.class);
		testers.add(Int2LongMapContainsKeyTester.class);
		testers.add(Int2LongMapContainsValueTester.class);
		testers.add(Int2LongMapEntrySetTester.class);
		testers.add(Int2LongMapEqualsTester.class);
		testers.add(Int2LongMapForEachTester.class);
		testers.add(Int2LongMapGetTester.class);
		testers.add(Int2LongMapGetOrDefaultTester.class);
		testers.add(Int2LongMapHashCodeTester.class);
		testers.add(Int2LongMapIsEmptyTester.class);
		testers.add(Int2LongMapMergeTester.class);
		testers.add(Int2LongMapPutTester.class);
		testers.add(Int2LongMapAddToTester.class);
		testers.add(Int2LongMapPutAllTester.class);
		testers.add(Int2LongMapPutAllArrayTester.class);
		testers.add(Int2LongMapPutIfAbsentTester.class);
		testers.add(Int2LongMapRemoveTester.class);
		testers.add(Int2LongMapRemoveEntryTester.class);
		testers.add(Int2LongMapRemoveOrDefaultTester.class);
		testers.add(Int2LongMapReplaceTester.class);
		testers.add(Int2LongMapReplaceAllTester.class);
		testers.add(Int2LongMapReplaceEntryTester.class);
		testers.add(Int2LongMapSizeTester.class);
		testers.add(Int2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Long>, Map.Entry<Integer, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2LongMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2LongMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2LongMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2LongMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
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