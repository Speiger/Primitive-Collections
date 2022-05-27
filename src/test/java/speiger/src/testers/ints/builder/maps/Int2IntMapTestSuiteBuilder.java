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
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2IntMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2IntMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2IntMapAddToTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Int2IntMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Integer> {
	public static Int2IntMapTestSuiteBuilder using(TestInt2IntMapGenerator generator) {
		return (Int2IntMapTestSuiteBuilder) new Int2IntMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2IntMapClearTester.class);
		testers.add(Int2IntMapComputeTester.class);
		testers.add(Int2IntMapComputeIfAbsentTester.class);
		testers.add(Int2IntMapComputeIfPresentTester.class);
		testers.add(Int2IntMapSupplyIfAbsentTester.class);
		testers.add(Int2IntMapContainsKeyTester.class);
		testers.add(Int2IntMapContainsValueTester.class);
		testers.add(Int2IntMapEntrySetTester.class);
		testers.add(Int2IntMapEqualsTester.class);
		testers.add(Int2IntMapForEachTester.class);
		testers.add(Int2IntMapGetTester.class);
		testers.add(Int2IntMapGetOrDefaultTester.class);
		testers.add(Int2IntMapHashCodeTester.class);
		testers.add(Int2IntMapIsEmptyTester.class);
		testers.add(Int2IntMapMergeTester.class);
		testers.add(Int2IntMapPutTester.class);
		testers.add(Int2IntMapAddToTester.class);
		testers.add(Int2IntMapPutAllTester.class);
		testers.add(Int2IntMapPutAllArrayTester.class);
		testers.add(Int2IntMapPutIfAbsentTester.class);
		testers.add(Int2IntMapRemoveTester.class);
		testers.add(Int2IntMapRemoveEntryTester.class);
		testers.add(Int2IntMapRemoveOrDefaultTester.class);
		testers.add(Int2IntMapReplaceTester.class);
		testers.add(Int2IntMapReplaceAllTester.class);
		testers.add(Int2IntMapReplaceEntryTester.class);
		testers.add(Int2IntMapSizeTester.class);
		testers.add(Int2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Integer>, Map.Entry<Integer, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2IntMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2IntMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2IntMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2IntMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
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