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
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ShortMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2ShortMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2ShortMapAddToTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Int2ShortMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Short> {
	public static Int2ShortMapTestSuiteBuilder using(TestInt2ShortMapGenerator generator) {
		return (Int2ShortMapTestSuiteBuilder) new Int2ShortMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2ShortMapClearTester.class);
		testers.add(Int2ShortMapComputeTester.class);
		testers.add(Int2ShortMapComputeIfAbsentTester.class);
		testers.add(Int2ShortMapComputeIfPresentTester.class);
		testers.add(Int2ShortMapSupplyIfAbsentTester.class);
		testers.add(Int2ShortMapContainsKeyTester.class);
		testers.add(Int2ShortMapContainsValueTester.class);
		testers.add(Int2ShortMapEntrySetTester.class);
		testers.add(Int2ShortMapEqualsTester.class);
		testers.add(Int2ShortMapForEachTester.class);
		testers.add(Int2ShortMapGetTester.class);
		testers.add(Int2ShortMapGetOrDefaultTester.class);
		testers.add(Int2ShortMapHashCodeTester.class);
		testers.add(Int2ShortMapIsEmptyTester.class);
		testers.add(Int2ShortMapMergeTester.class);
		testers.add(Int2ShortMapPutTester.class);
		testers.add(Int2ShortMapAddToTester.class);
		testers.add(Int2ShortMapPutAllTester.class);
		testers.add(Int2ShortMapPutAllArrayTester.class);
		testers.add(Int2ShortMapPutIfAbsentTester.class);
		testers.add(Int2ShortMapRemoveTester.class);
		testers.add(Int2ShortMapRemoveEntryTester.class);
		testers.add(Int2ShortMapRemoveOrDefaultTester.class);
		testers.add(Int2ShortMapReplaceTester.class);
		testers.add(Int2ShortMapReplaceAllTester.class);
		testers.add(Int2ShortMapReplaceEntryTester.class);
		testers.add(Int2ShortMapSizeTester.class);
		testers.add(Int2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Short>, Map.Entry<Integer, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2ShortMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2ShortMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2ShortMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2ShortMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
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