package com.feng.jdk.java8;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @since 2025/7/30
 */
public class SteamTest {

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(
				Stream.of("green", "blue", "yellow").max((s1, s2) -> s1.compareTo(s2))
						.filter(s -> s.endsWith("n")).orElse("yellow"));

		Collector<String, ?, TreeMap<Integer, String>> myCollector = Collector.of(
				TreeMap::new,
				(map, str) -> map.put(str.length(), str.toLowerCase()),
				(map1, map2) -> {
					map1.putAll(map2);
					return map1;
				}
		);
		Collector<String, ?, HashMap<Integer, String>> myCollector2 = Collector.of(
				HashMap::new,
				(map, str) -> map.put(str.length(), str.toLowerCase()),
				(map1, map2) -> {
					map1.putAll(map2);
					return map1;
				}
		);
		Collector<String, ?, AbstractMap<Integer, String>> myCollector3 = Collector.of(
				HashMap::new,
				(map, str) -> map.put(str.length(), str.toLowerCase()),
				(map1, map2) -> {
					map1.putAll(map2);
					return map1;
				}
		);
		TreeMap<Integer, String> treeMap =
				Stream.of("green", "blue", "yellow").collect(myCollector);
		HashMap<Integer, String> hashMap =
				Stream.of("green", "blue", "yellow").collect(myCollector2);
		AbstractMap<Integer, String> abstractMap =
				Stream.of("green", "blue", "yellow").collect(myCollector3);
		treeMap.forEach((k, v) -> System.out.println(k + ":" + v));
		hashMap.forEach((k, v) -> System.out.println(k + ":" + v));
		abstractMap.forEach((k, v) -> System.out.println(k + ":" + v));

		List<String> strings = Arrays.asList("dog", "over", "good");
		String reduce = strings.stream().reduce(new String(), (s1, s2) -> {
			if (s1.equals("dog")) {
				return s1;
			}
			else {
				return s2;
			}
		});
		String reduce2 = strings.stream().reduce(new String(), (s1, s2) ->
				s1 + s2.charAt(0), (s1, s2) -> s1 + s2);
		System.out.println(reduce);
		System.out.println(reduce2);

		new BufferedReader(new FileReader(""));
		new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in)));
	}
}
