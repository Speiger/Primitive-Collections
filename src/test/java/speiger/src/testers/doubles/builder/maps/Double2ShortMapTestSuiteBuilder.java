package speiger.src.testers.doubles.builder.maps;

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
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ShortMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2ShortMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapAddToTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Double2ShortMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Short> {
	public static Double2ShortMapTestSuiteBuilder using(TestDouble2ShortMapGenerator generator) {
		return (Double2ShortMapTestSuiteBuilder) new Double2ShortMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2ShortMapClearTester.class);
		testers.add(Double2ShortMapComputeTester.class);
		testers.add(Double2ShortMapComputeIfAbsentTester.class);
		testers.add(Double2ShortMapComputeIfPresentTester.class);
		testers.add(Double2ShortMapSupplyIfAbsentTester.class);
		testers.add(Double2ShortMapContainsKeyTester.class);
		testers.add(Double2ShortMapContainsValueTester.class);
		testers.add(Double2ShortMapEntrySetTester.class);
		testers.add(Double2ShortMapEqualsTester.class);
		testers.add(Double2ShortMapForEachTester.class);
		testers.add(Double2ShortMapGetTester.class);
		testers.add(Double2ShortMapGetOrDefaultTester.class);
		testers.add(Double2ShortMapHashCodeTester.class);
		testers.add(Double2ShortMapIsEmptyTester.class);
		testers.add(Double2ShortMapMergeTester.class);
		testers.add(Double2ShortMapPutTester.class);
		testers.add(Double2ShortMapAddToTester.class);
		testers.add(Double2ShortMapPutAllTester.class);
		testers.add(Double2ShortMapPutAllArrayTester.class);
		testers.add(Double2ShortMapPutIfAbsentTester.class);
		testers.add(Double2ShortMapRemoveTester.class);
		testers.add(Double2ShortMapRemoveEntryTester.class);
		testers.add(Double2ShortMapRemoveOrDefaultTester.class);
		testers.add(Double2ShortMapReplaceTester.class);
		testers.add(Double2ShortMapReplaceAllTester.class);
		testers.add(Double2ShortMapReplaceEntryTester.class);
		testers.add(Double2ShortMapSizeTester.class);
		testers.add(Double2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Short>, Map.Entry<Double, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2ShortMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2ShortMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2ShortMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2ShortMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
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