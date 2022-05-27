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
import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2FloatMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2FloatMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapAddToTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Double2FloatMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Float> {
	public static Double2FloatMapTestSuiteBuilder using(TestDouble2FloatMapGenerator generator) {
		return (Double2FloatMapTestSuiteBuilder) new Double2FloatMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2FloatMapClearTester.class);
		testers.add(Double2FloatMapComputeTester.class);
		testers.add(Double2FloatMapComputeIfAbsentTester.class);
		testers.add(Double2FloatMapComputeIfPresentTester.class);
		testers.add(Double2FloatMapSupplyIfAbsentTester.class);
		testers.add(Double2FloatMapContainsKeyTester.class);
		testers.add(Double2FloatMapContainsValueTester.class);
		testers.add(Double2FloatMapEntrySetTester.class);
		testers.add(Double2FloatMapEqualsTester.class);
		testers.add(Double2FloatMapForEachTester.class);
		testers.add(Double2FloatMapGetTester.class);
		testers.add(Double2FloatMapGetOrDefaultTester.class);
		testers.add(Double2FloatMapHashCodeTester.class);
		testers.add(Double2FloatMapIsEmptyTester.class);
		testers.add(Double2FloatMapMergeTester.class);
		testers.add(Double2FloatMapPutTester.class);
		testers.add(Double2FloatMapAddToTester.class);
		testers.add(Double2FloatMapPutAllTester.class);
		testers.add(Double2FloatMapPutAllArrayTester.class);
		testers.add(Double2FloatMapPutIfAbsentTester.class);
		testers.add(Double2FloatMapRemoveTester.class);
		testers.add(Double2FloatMapRemoveEntryTester.class);
		testers.add(Double2FloatMapRemoveOrDefaultTester.class);
		testers.add(Double2FloatMapReplaceTester.class);
		testers.add(Double2FloatMapReplaceAllTester.class);
		testers.add(Double2FloatMapReplaceEntryTester.class);
		testers.add(Double2FloatMapSizeTester.class);
		testers.add(Double2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Float>, Map.Entry<Double, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2FloatMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2FloatMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2FloatMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2FloatMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
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
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}