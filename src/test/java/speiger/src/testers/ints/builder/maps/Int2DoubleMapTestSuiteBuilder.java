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
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2DoubleMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2DoubleMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapAddToTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Int2DoubleMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Double> {
	public static Int2DoubleMapTestSuiteBuilder using(TestInt2DoubleMapGenerator generator) {
		return (Int2DoubleMapTestSuiteBuilder) new Int2DoubleMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2DoubleMapClearTester.class);
		testers.add(Int2DoubleMapComputeTester.class);
		testers.add(Int2DoubleMapComputeIfAbsentTester.class);
		testers.add(Int2DoubleMapComputeIfPresentTester.class);
		testers.add(Int2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Int2DoubleMapContainsKeyTester.class);
		testers.add(Int2DoubleMapContainsValueTester.class);
		testers.add(Int2DoubleMapEntrySetTester.class);
		testers.add(Int2DoubleMapEqualsTester.class);
		testers.add(Int2DoubleMapForEachTester.class);
		testers.add(Int2DoubleMapGetTester.class);
		testers.add(Int2DoubleMapGetOrDefaultTester.class);
		testers.add(Int2DoubleMapHashCodeTester.class);
		testers.add(Int2DoubleMapIsEmptyTester.class);
		testers.add(Int2DoubleMapMergeTester.class);
		testers.add(Int2DoubleMapPutTester.class);
		testers.add(Int2DoubleMapAddToTester.class);
		testers.add(Int2DoubleMapPutAllTester.class);
		testers.add(Int2DoubleMapPutAllArrayTester.class);
		testers.add(Int2DoubleMapPutIfAbsentTester.class);
		testers.add(Int2DoubleMapRemoveTester.class);
		testers.add(Int2DoubleMapRemoveEntryTester.class);
		testers.add(Int2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Int2DoubleMapReplaceTester.class);
		testers.add(Int2DoubleMapReplaceAllTester.class);
		testers.add(Int2DoubleMapReplaceEntryTester.class);
		testers.add(Int2DoubleMapSizeTester.class);
		testers.add(Int2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Double>, Map.Entry<Integer, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2DoubleMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2DoubleMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2DoubleMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2DoubleMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
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