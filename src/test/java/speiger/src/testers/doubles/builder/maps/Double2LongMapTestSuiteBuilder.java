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
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2LongMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2LongMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2LongMapAddToTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Double2LongMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Long> {
	public static Double2LongMapTestSuiteBuilder using(TestDouble2LongMapGenerator generator) {
		return (Double2LongMapTestSuiteBuilder) new Double2LongMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2LongMapClearTester.class);
		testers.add(Double2LongMapComputeTester.class);
		testers.add(Double2LongMapComputeIfAbsentTester.class);
		testers.add(Double2LongMapComputeIfPresentTester.class);
		testers.add(Double2LongMapSupplyIfAbsentTester.class);
		testers.add(Double2LongMapContainsKeyTester.class);
		testers.add(Double2LongMapContainsValueTester.class);
		testers.add(Double2LongMapEntrySetTester.class);
		testers.add(Double2LongMapEqualsTester.class);
		testers.add(Double2LongMapForEachTester.class);
		testers.add(Double2LongMapGetTester.class);
		testers.add(Double2LongMapGetOrDefaultTester.class);
		testers.add(Double2LongMapHashCodeTester.class);
		testers.add(Double2LongMapIsEmptyTester.class);
		testers.add(Double2LongMapMergeTester.class);
		testers.add(Double2LongMapPutTester.class);
		testers.add(Double2LongMapAddToTester.class);
		testers.add(Double2LongMapPutAllTester.class);
		testers.add(Double2LongMapPutAllArrayTester.class);
		testers.add(Double2LongMapPutIfAbsentTester.class);
		testers.add(Double2LongMapRemoveTester.class);
		testers.add(Double2LongMapRemoveEntryTester.class);
		testers.add(Double2LongMapRemoveOrDefaultTester.class);
		testers.add(Double2LongMapReplaceTester.class);
		testers.add(Double2LongMapReplaceAllTester.class);
		testers.add(Double2LongMapReplaceEntryTester.class);
		testers.add(Double2LongMapSizeTester.class);
		testers.add(Double2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Long>, Map.Entry<Double, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2LongMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2LongMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2LongMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2LongMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
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
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}