package speiger.src.testers.floats.builder.maps;

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
import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.testers.ints.builder.IntCollectionTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2IntMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2IntMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2IntMapAddToTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2IntMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Float2IntMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Integer> {
	public static Float2IntMapTestSuiteBuilder using(TestFloat2IntMapGenerator generator) {
		return (Float2IntMapTestSuiteBuilder) new Float2IntMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2IntMapClearTester.class);
		testers.add(Float2IntMapComputeTester.class);
		testers.add(Float2IntMapComputeIfAbsentTester.class);
		testers.add(Float2IntMapComputeIfPresentTester.class);
		testers.add(Float2IntMapSupplyIfAbsentTester.class);
		testers.add(Float2IntMapContainsKeyTester.class);
		testers.add(Float2IntMapContainsValueTester.class);
		testers.add(Float2IntMapEntrySetTester.class);
		testers.add(Float2IntMapEqualsTester.class);
		testers.add(Float2IntMapForEachTester.class);
		testers.add(Float2IntMapGetTester.class);
		testers.add(Float2IntMapGetOrDefaultTester.class);
		testers.add(Float2IntMapHashCodeTester.class);
		testers.add(Float2IntMapIsEmptyTester.class);
		testers.add(Float2IntMapMergeTester.class);
		testers.add(Float2IntMapPutTester.class);
		testers.add(Float2IntMapAddToTester.class);
		testers.add(Float2IntMapPutAllTester.class);
		testers.add(Float2IntMapPutAllArrayTester.class);
		testers.add(Float2IntMapPutIfAbsentTester.class);
		testers.add(Float2IntMapRemoveTester.class);
		testers.add(Float2IntMapRemoveEntryTester.class);
		testers.add(Float2IntMapRemoveOrDefaultTester.class);
		testers.add(Float2IntMapReplaceTester.class);
		testers.add(Float2IntMapReplaceAllTester.class);
		testers.add(Float2IntMapReplaceEntryTester.class);
		testers.add(Float2IntMapSizeTester.class);
		testers.add(Float2IntMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Integer>, Map.Entry<Float, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2IntMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2IntMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2IntMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2IntMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2IntMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
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