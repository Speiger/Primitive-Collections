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
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2BooleanMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2BooleanMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Double2BooleanMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Boolean> {
	public static Double2BooleanMapTestSuiteBuilder using(TestDouble2BooleanMapGenerator generator) {
		return (Double2BooleanMapTestSuiteBuilder) new Double2BooleanMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2BooleanMapClearTester.class);
		testers.add(Double2BooleanMapComputeTester.class);
		testers.add(Double2BooleanMapComputeIfAbsentTester.class);
		testers.add(Double2BooleanMapComputeIfPresentTester.class);
		testers.add(Double2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Double2BooleanMapContainsKeyTester.class);
		testers.add(Double2BooleanMapContainsValueTester.class);
		testers.add(Double2BooleanMapEntrySetTester.class);
		testers.add(Double2BooleanMapEqualsTester.class);
		testers.add(Double2BooleanMapForEachTester.class);
		testers.add(Double2BooleanMapGetTester.class);
		testers.add(Double2BooleanMapGetOrDefaultTester.class);
		testers.add(Double2BooleanMapHashCodeTester.class);
		testers.add(Double2BooleanMapIsEmptyTester.class);
		testers.add(Double2BooleanMapMergeTester.class);
		testers.add(Double2BooleanMapPutTester.class);
		testers.add(Double2BooleanMapPutAllTester.class);
		testers.add(Double2BooleanMapPutAllArrayTester.class);
		testers.add(Double2BooleanMapPutIfAbsentTester.class);
		testers.add(Double2BooleanMapRemoveTester.class);
		testers.add(Double2BooleanMapRemoveEntryTester.class);
		testers.add(Double2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Double2BooleanMapReplaceTester.class);
		testers.add(Double2BooleanMapReplaceAllTester.class);
		testers.add(Double2BooleanMapReplaceEntryTester.class);
		testers.add(Double2BooleanMapSizeTester.class);
		testers.add(Double2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Boolean>, Map.Entry<Double, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2BooleanMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2BooleanMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2BooleanMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
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
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}