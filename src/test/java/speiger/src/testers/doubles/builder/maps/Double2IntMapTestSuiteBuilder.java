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
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2IntMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2IntMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2IntMapAddToTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Double2IntMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Integer> {
	public static Double2IntMapTestSuiteBuilder using(TestDouble2IntMapGenerator generator) {
		return (Double2IntMapTestSuiteBuilder) new Double2IntMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2IntMapClearTester.class);
		testers.add(Double2IntMapComputeTester.class);
		testers.add(Double2IntMapComputeIfAbsentTester.class);
		testers.add(Double2IntMapComputeIfPresentTester.class);
		testers.add(Double2IntMapSupplyIfAbsentTester.class);
		testers.add(Double2IntMapContainsKeyTester.class);
		testers.add(Double2IntMapContainsValueTester.class);
		testers.add(Double2IntMapEntrySetTester.class);
		testers.add(Double2IntMapEqualsTester.class);
		testers.add(Double2IntMapForEachTester.class);
		testers.add(Double2IntMapGetTester.class);
		testers.add(Double2IntMapGetOrDefaultTester.class);
		testers.add(Double2IntMapHashCodeTester.class);
		testers.add(Double2IntMapIsEmptyTester.class);
		testers.add(Double2IntMapMergeTester.class);
		testers.add(Double2IntMapPutTester.class);
		testers.add(Double2IntMapAddToTester.class);
		testers.add(Double2IntMapPutAllTester.class);
		testers.add(Double2IntMapPutAllArrayTester.class);
		testers.add(Double2IntMapPutIfAbsentTester.class);
		testers.add(Double2IntMapRemoveTester.class);
		testers.add(Double2IntMapRemoveEntryTester.class);
		testers.add(Double2IntMapRemoveOrDefaultTester.class);
		testers.add(Double2IntMapReplaceTester.class);
		testers.add(Double2IntMapReplaceAllTester.class);
		testers.add(Double2IntMapReplaceEntryTester.class);
		testers.add(Double2IntMapSizeTester.class);
		testers.add(Double2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Integer>, Map.Entry<Double, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2IntMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2IntMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2IntMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2IntMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
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
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}