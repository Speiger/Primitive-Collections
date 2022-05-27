package speiger.src.testers.bytes.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleSortedMap;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.bytes.generators.TestByteNavigableSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2DoubleMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2DoubleSortedMapGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;
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
public class DerivedByte2DoubleMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestByte2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Byte2DoubleNavigableMap createSubMap(Byte2DoubleSortedMap sortedMap, byte firstExclusive, byte lastExclusive) {
	    	Byte2DoubleNavigableMap map = (Byte2DoubleNavigableMap) sortedMap;
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
	        return (Byte2DoubleNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestByte2DoubleSortedMapGenerator {
		TestByte2DoubleSortedMapGenerator parent;
		Bound to;
		Bound from;
		byte firstInclusive;
		byte lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestByte2DoubleSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Byte2DoubleSortedMap map = parent.create();
			entryComparator = DerivedByte2DoubleMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getByteKey();
			lastInclusive = samples.get(samples.size() - 1).getByteKey();
		}
		
		@Override
		public Byte2DoubleSortedMap create(Entry... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getByteKey(), parent.aboveSamplesLesser().getByteKey());
		}
		
		Byte2DoubleSortedMap createSubMap(Byte2DoubleSortedMap map, byte firstExclusive, byte lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestByte2DoubleSortedMapGenerator
	{
		TestByte2DoubleSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestByte2DoubleSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Byte2DoubleNavigableMap create(Entry... elements) {
			return ((Byte2DoubleNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Byte, Double>> order(List<Map.Entry<Byte, Double>> insertionOrder) {
			ObjectList<Map.Entry<Byte, Double>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestByte2DoubleMapGenerator
	{
		TestByte2DoubleMapGenerator parent;
		
		public MapGenerator(TestByte2DoubleMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Byte, Double>> order(List<Map.Entry<Byte, Double>> insertionOrder) {
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
		public Byte2DoubleMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Byte2DoubleMap.Entry> {
		TestByte2DoubleMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>> inner) {
			generator = (TestByte2DoubleMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).byte2DoubleEntrySet();
		}
	}

	public static TestByteSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>> inner) {
		if (inner.getInnerGenerator() instanceof TestByte2DoubleSortedMapGenerator) {
			ByteSet set = ((TestByte2DoubleSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ByteNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof ByteSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestByteNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>> inner) {
			super(inner);
		}
		
		@Override
		public ByteNavigableSet create(byte... elements) {
			return (ByteNavigableSet) super.create(elements);
		}

		@Override
		public ByteNavigableSet create(Object... elements) {
			return (ByteNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestByteSortedSetGenerator {
		TestByte2DoubleSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>> inner) {
			super(inner);
			generator = (TestByte2DoubleSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ByteSortedSet create(byte... elements) {
			return (ByteSortedSet) super.create(elements);
		}

		@Override
		public ByteSortedSet create(Object... elements) {
			return (ByteSortedSet) super.create(elements);
		}

		@Override
		public byte belowSamplesLesser() {
			return generator.belowSamplesLesser().getByteKey();
		}

		@Override
		public byte belowSamplesGreater() {
			return generator.belowSamplesGreater().getByteKey();
		}

		@Override
		public byte aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getByteKey();
		}

		@Override
		public byte aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getByteKey();
		}
	}

	public static class MapKeySetGenerator implements TestByteSetGenerator {
		TestByte2DoubleMapGenerator generator;
		ByteSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>> inner) {
			generator = (TestByte2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Byte2DoubleMap.Entry> samples = generator.getSamples();
			this.samples = new ByteSamples(samples.e0().getByteKey(), samples.e1().getByteKey(), samples.e2().getByteKey(), samples.e3().getByteKey(), samples.e4().getByteKey());
		}

		@Override
		public ByteSamples getSamples() {
			return samples;
		}

		@Override
		public ByteIterable order(ByteList insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2DoubleMap.BasicEntry(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}

		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			double value = generator.getSamples().e0().getDoubleValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2DoubleMap.BasicEntry(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}
		
		@Override
		public ByteSet create(byte... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2DoubleMap.BasicEntry(elements[index++], entry.getDoubleValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public ByteSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2DoubleMap.BasicEntry((Byte) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestDoubleCollectionGenerator {
		TestByte2DoubleMapGenerator generator;
		DoubleSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>> inner) {
			generator = (TestByte2DoubleMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Byte2DoubleMap.Entry> samples = generator.getSamples();
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
					throw new IllegalArgumentException("Byte2DoubleMap.values generator can order only sample values");
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
					throw new IllegalArgumentException("Byte2DoubleMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public DoubleCollection create(double... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2DoubleMap.BasicEntry(entry.getByteKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public DoubleCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2DoubleMap.BasicEntry(entry.getKey(), (Double)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Byte, Double>> entryObjectComparator(Comparator<Byte> keyComparator) {
		return new Comparator<Map.Entry<Byte, Double>>() {
			@Override
			public int compare(Map.Entry<Byte, Double> a, Map.Entry<Byte, Double> b) {
				if(keyComparator == null) {
					return Byte.compare(a.getKey().byteValue(), b.getKey().byteValue());
				}
				return keyComparator.compare(a.getKey().byteValue(), b.getKey().byteValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(ByteComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Byte.compare(a.getByteKey(), b.getByteKey());
				}
				return keyComparator.compare(a.getByteKey(), b.getByteKey());
			}
		};
	}
}