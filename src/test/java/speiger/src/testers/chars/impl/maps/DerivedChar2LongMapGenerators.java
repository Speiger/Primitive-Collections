package speiger.src.testers.chars.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap.Entry;
import speiger.src.collections.chars.maps.interfaces.Char2LongNavigableMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongSortedMap;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.chars.generators.TestCharNavigableSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2LongMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2LongSortedMapGenerator;
import speiger.src.testers.chars.utils.CharSamples;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.longs.utils.LongSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedChar2LongMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestChar2LongSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Char2LongNavigableMap createSubMap(Char2LongSortedMap sortedMap, char firstExclusive, char lastExclusive) {
	    	Char2LongNavigableMap map = (Char2LongNavigableMap) sortedMap;
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
	        return (Char2LongNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestChar2LongSortedMapGenerator {
		TestChar2LongSortedMapGenerator parent;
		Bound to;
		Bound from;
		char firstInclusive;
		char lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestChar2LongSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Char2LongSortedMap map = parent.create();
			entryComparator = DerivedChar2LongMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getCharKey();
			lastInclusive = samples.get(samples.size() - 1).getCharKey();
		}
		
		@Override
		public Char2LongSortedMap create(Entry... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getCharKey(), parent.aboveSamplesLesser().getCharKey());
		}
		
		Char2LongSortedMap createSubMap(Char2LongSortedMap map, char firstExclusive, char lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestChar2LongSortedMapGenerator
	{
		TestChar2LongSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestChar2LongSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Char2LongNavigableMap create(Entry... elements) {
			return ((Char2LongNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Character, Long>> order(List<Map.Entry<Character, Long>> insertionOrder) {
			ObjectList<Map.Entry<Character, Long>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestChar2LongMapGenerator
	{
		TestChar2LongMapGenerator parent;
		
		public MapGenerator(TestChar2LongMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Character, Long>> order(List<Map.Entry<Character, Long>> insertionOrder) {
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
		public Char2LongMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Char2LongMap.Entry> {
		TestChar2LongMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>> inner) {
			generator = (TestChar2LongMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).char2LongEntrySet();
		}
	}

	public static TestCharSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>> inner) {
		if (inner.getInnerGenerator() instanceof TestChar2LongSortedMapGenerator) {
			CharSet set = ((TestChar2LongSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof CharNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof CharSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestCharNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>> inner) {
			super(inner);
		}
		
		@Override
		public CharNavigableSet create(char... elements) {
			return (CharNavigableSet) super.create(elements);
		}

		@Override
		public CharNavigableSet create(Object... elements) {
			return (CharNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestCharSortedSetGenerator {
		TestChar2LongSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>> inner) {
			super(inner);
			generator = (TestChar2LongSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public CharSortedSet create(char... elements) {
			return (CharSortedSet) super.create(elements);
		}

		@Override
		public CharSortedSet create(Object... elements) {
			return (CharSortedSet) super.create(elements);
		}

		@Override
		public char belowSamplesLesser() {
			return generator.belowSamplesLesser().getCharKey();
		}

		@Override
		public char belowSamplesGreater() {
			return generator.belowSamplesGreater().getCharKey();
		}

		@Override
		public char aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getCharKey();
		}

		@Override
		public char aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getCharKey();
		}
	}

	public static class MapKeySetGenerator implements TestCharSetGenerator {
		TestChar2LongMapGenerator generator;
		CharSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>> inner) {
			generator = (TestChar2LongMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Char2LongMap.Entry> samples = generator.getSamples();
			this.samples = new CharSamples(samples.e0().getCharKey(), samples.e1().getCharKey(), samples.e2().getCharKey(), samples.e3().getCharKey(), samples.e4().getCharKey());
		}

		@Override
		public CharSamples getSamples() {
			return samples;
		}

		@Override
		public CharIterable order(CharList insertionOrder) {
			long value = generator.getSamples().e0().getLongValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2LongMap.BasicEntry(key, value));
			}
			CharList list = new CharArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getCharKey());
			}
			return list;
		}

		@Override
		public Iterable<Character> order(List<Character> insertionOrder) {
			long value = generator.getSamples().e0().getLongValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (char key : insertionOrder) {
				entries.add(new AbstractChar2LongMap.BasicEntry(key, value));
			}
			CharList list = new CharArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getCharKey());
			}
			return list;
		}
		
		@Override
		public CharSet create(char... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2LongMap.BasicEntry(elements[index++], entry.getLongValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public CharSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2LongMap.BasicEntry((Character) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestLongCollectionGenerator {
		TestChar2LongMapGenerator generator;
		LongSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>> inner) {
			generator = (TestChar2LongMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Char2LongMap.Entry> samples = generator.getSamples();
			this.samples = new LongSamples(samples.e0().getLongValue(), samples.e1().getLongValue(), samples.e2().getLongValue(), samples.e3().getLongValue(), samples.e4().getLongValue());
		}
		
		@Override
		public LongSamples getSamples() {
			return samples;
		}

		@Override
		public LongIterable order(LongList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new LongComparator() {
				@Override
				public int compare(long key, long value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(long entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getLongValue() == entry) return i;
					}
					throw new IllegalArgumentException("Char2LongMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Long> order(List<Long> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Long>() {
				@Override
				public int compare(Long key, Long value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Long entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getLongValue() == entry.longValue()) return i;
					}
					throw new IllegalArgumentException("Char2LongMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public LongCollection create(long... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2LongMap.BasicEntry(entry.getCharKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public LongCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractChar2LongMap.BasicEntry(entry.getKey(), (Long)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Character, Long>> entryObjectComparator(Comparator<Character> keyComparator) {
		return new Comparator<Map.Entry<Character, Long>>() {
			@Override
			public int compare(Map.Entry<Character, Long> a, Map.Entry<Character, Long> b) {
				if(keyComparator == null) {
					return Character.compare(a.getKey().charValue(), b.getKey().charValue());
				}
				return keyComparator.compare(a.getKey().charValue(), b.getKey().charValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(CharComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Character.compare(a.getCharKey(), b.getCharKey());
				}
				return keyComparator.compare(a.getCharKey(), b.getCharKey());
			}
		};
	}
}