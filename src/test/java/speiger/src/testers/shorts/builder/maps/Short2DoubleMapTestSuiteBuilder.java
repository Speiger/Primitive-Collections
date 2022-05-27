package speiger.src.testers.shorts.builder.maps;

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
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2DoubleMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2DoubleMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapAddToTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Short2DoubleMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Double> {
	public static Short2DoubleMapTestSuiteBuilder using(TestShort2DoubleMapGenerator generator) {
		return (Short2DoubleMapTestSuiteBuilder) new Short2DoubleMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2DoubleMapClearTester.class);
		testers.add(Short2DoubleMapComputeTester.class);
		testers.add(Short2DoubleMapComputeIfAbsentTester.class);
		testers.add(Short2DoubleMapComputeIfPresentTester.class);
		testers.add(Short2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Short2DoubleMapContainsKeyTester.class);
		testers.add(Short2DoubleMapContainsValueTester.class);
		testers.add(Short2DoubleMapEntrySetTester.class);
		testers.add(Short2DoubleMapEqualsTester.class);
		testers.add(Short2DoubleMapForEachTester.class);
		testers.add(Short2DoubleMapGetTester.class);
		testers.add(Short2DoubleMapGetOrDefaultTester.class);
		testers.add(Short2DoubleMapHashCodeTester.class);
		testers.add(Short2DoubleMapIsEmptyTester.class);
		testers.add(Short2DoubleMapMergeTester.class);
		testers.add(Short2DoubleMapPutTester.class);
		testers.add(Short2DoubleMapAddToTester.class);
		testers.add(Short2DoubleMapPutAllTester.class);
		testers.add(Short2DoubleMapPutAllArrayTester.class);
		testers.add(Short2DoubleMapPutIfAbsentTester.class);
		testers.add(Short2DoubleMapRemoveTester.class);
		testers.add(Short2DoubleMapRemoveEntryTester.class);
		testers.add(Short2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Short2DoubleMapReplaceTester.class);
		testers.add(Short2DoubleMapReplaceAllTester.class);
		testers.add(Short2DoubleMapReplaceEntryTester.class);
		testers.add(Short2DoubleMapSizeTester.class);
		testers.add(Short2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Double>, Map.Entry<Short, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2DoubleMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2DoubleMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2DoubleMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2DoubleMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
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