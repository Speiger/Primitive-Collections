package speiger.src.testers.floats.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap.Entry;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleNavigableMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleSortedMap;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.floats.generators.TestFloatNavigableSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2DoubleMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2DoubleSortedMapGenerator;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedFloat2DoubleMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestFloat2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Float2DoubleNavigableMap createSubMap(Float2DoubleSortedMap sortedMap, float firstExclusive, float lastExclusive) {
	    	Float2DoubleNavigableMap map = (Float2DoubleNavigableMap) sortedMap;
	      if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) {
	        return map.headMap(lastInclusive, true);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) {
	        return map.tailMap(firstExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastInclusive, true);
	      } else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstInclusive, true, lastInclusive, true);
	      } else {
	        return (Float2DoubleNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestFloat2DoubleSortedMapGenerator {
		TestFloat2DoubleSortedMapGenerator parent;
		Bound to;
		Bound from;
		float firstInclusive;
		float lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestFloat2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Float2DoubleSortedMap map = parent.create();
			entryComparator = DerivedFloat2DoubleMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getFloatKey();
			lastInclusive = samples.get(samples.size() - 1).getFloatKey();
		}
		
		@Override
		public Float2DoubleSortedMap create(Entry... elements) {
			ObjectList<Entry> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getFloatKey(), parent.aboveSamplesLesser().getFloatKey());
		}
		
		Float2DoubleSortedMap createSubMap(Float2DoubleSortedMap map, float firstExclusive, float lastExclusive) {
			if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
				return map.headMap(lastExclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
				return map.tailMap(firstInclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
				return map.subMap(firstInclusive, lastExclusive);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Entry belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestFloat2DoubleSortedMapGenerator
	{
		TestFloat2DoubleSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestFloat2DoubleSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Float2DoubleNavigableMap create(Entry... elements) {
			return ((Float2DoubleNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Float, Double>> order(List<Map.Entry<Float, Double>> insertionOrder) {
			ObjectList<Map.Entry<Float, Double>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			ObjectList<Entry> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator implements TestFloat2DoubleMapGenerator
	{
		TestFloat2DoubleMapGenerator parent;
		
		public MapGenerator(TestFloat2DoubleMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Float, Double>> order(List<Map.Entry<Float, Double>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Float2DoubleMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Float2DoubleMap.Entry> {
		TestFloat2DoubleMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Float, Double>, Map.Entry<Float, Double>> inner) {
			generator = (TestFloat2DoubleMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry> order(List<Entry> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry>(insertionOrder));
		}

		@Override
		public Entry[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry> create(Object... elements) {
			return generator.create(elements).float2DoubleEntrySet();
		}
	}

	public static TestFloatSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Float, Double>, Map.Entry<Float, Double>> inner) {
		if (inner.getInnerGenerator() instanceof TestFloat2DoubleSortedMapGenerator) {
			FloatSet set = ((TestFloat2DoubleSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof FloatNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof FloatSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestFloatNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, Double>, Map.Entry<Float, Double>> inner) {
			super(inner);
		}
		
		@Override
		public FloatNavigableSet create(float... elements) {
			return (FloatNavigableSet) super.create(elements);
		}

		@Override
		public FloatNavigableSet create(Object... elements) {
			return (FloatNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestFloatSortedSetGenerator {
		TestFloat2DoubleSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, Double>, Map.Entry<Float, Double>> inner) {
			super(inner);
			generator = (TestFloat2DoubleSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public FloatSortedSet create(float... elements) {
			return (FloatSortedSet) super.create(elements);
		}

		@Override
		public FloatSortedSet create(Object... elements) {
			return (FloatSortedSet) super.create(elements);
		}

		@Override
		public float belowSamplesLesser() {
			return generator.belowSamplesLesser().getFloatKey();
		}

		@Override
		public float belowSamplesGreater() {
			return generator.belowSamplesGreater().getFloatKey();
		}

		@Override
		public float aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getFloatKey();
		}

		@Override
		public float aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getFloatKey();
		}
	}

	public static class MapKeySetGenerator implements TestFloatSetGenerator {
		TestFloat2DoubleMapGenerator generator;
		FloatSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Float, Double>, Map.Entry<Float, Double>> inner) {
			generator = (TestFloat2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Float2DoubleMap.Entry> samples = generator.getSamples();
			this.samples = new FloatSamples(samples.e0().getFloatKey(), samples.e1().getFloatKey(), samples.e2().getFloatKey(), samples.e3().getFloatKey(), samples.e4().getFloatKey());
		}

		@Override
		public FloatSamples getSamples() {
			return samples;
		}

		@Override
		public FloatIterable order(FloatList insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (float key : insertionOrder) {
				entries.add(new AbstractFloat2DoubleMap.BasicEntry(key, value));
			}
			FloatList list = new FloatArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getFloatKey());
			}
			return list;
		}

		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (float key : insertionOrder) {
				entries.add(new AbstractFloat2DoubleMap.BasicEntry(key, value));
			}
			FloatList list = new FloatArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getFloatKey());
			}
			return list;
		}
		
		@Override
		public FloatSet create(float... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2DoubleMap.BasicEntry(elements[index++], entry.getDoubleValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public FloatSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2DoubleMap.BasicEntry((Float) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestDoubleCollectionGenerator {
		TestFloat2DoubleMapGenerator generator;
		DoubleSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Float, Double>, Map.Entry<Float, Double>> inner) {
			generator = (TestFloat2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Float2DoubleMap.Entry> samples = generator.getSamples();
			this.samples = new DoubleSamples(samples.e0().getDoubleValue(), samples.e1().getDoubleValue(), samples.e2().getDoubleValue(), samples.e3().getDoubleValue(), samples.e4().getDoubleValue());
		}
		
		@Override
		public DoubleSamples getSamples() {
			return samples;
		}

		@Override
		public DoubleIterable order(DoubleList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new DoubleComparator() {
				@Override
				public int compare(double key, double value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(double entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Double.doubleToLongBits(list.get(i).getDoubleValue()) == Double.doubleToLongBits(entry)) return i;
					}
					throw new IllegalArgumentException("Float2DoubleMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Double> order(List<Double> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Double>() {
				@Override
				public int compare(Double key, Double value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Double entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Double.doubleToLongBits(list.get(i).getDoubleValue()) == Double.doubleToLongBits(entry.doubleValue())) return i;
					}
					throw new IllegalArgumentException("Float2DoubleMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public DoubleCollection create(double... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2DoubleMap.BasicEntry(entry.getFloatKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public DoubleCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractFloat2DoubleMap.BasicEntry(entry.getKey(), (Double)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Float, Double>> entryObjectComparator(Comparator<Float> keyComparator) {
		return new Comparator<Map.Entry<Float, Double>>() {
			@Override
			public int compare(Map.Entry<Float, Double> a, Map.Entry<Float, Double> b) {
				if(keyComparator == null) {
					return Float.compare(a.getKey().floatValue(), b.getKey().floatValue());
				}
				return keyComparator.compare(a.getKey().floatValue(), b.getKey().floatValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(FloatComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Float.compare(a.getFloatKey(), b.getFloatKey());
				}
				return keyComparator.compare(a.getFloatKey(), b.getFloatKey());
			}
		};
	}
}