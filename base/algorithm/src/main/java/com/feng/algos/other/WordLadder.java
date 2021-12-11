package com.feng.algos.other;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @Description leecode 127. 单词接龙
 * @Author fengsy
 * @Date 12/8/21
 */
public class WordLadder {
    /**
     * @param beginWord dog
     * @param endWord   cat
     * @param wordList  ["dag","cag","cat","cot"]
     * @return 转换需要步骤数 ，例如：dog -> dag -> cag -> cat     从dog转换到cat共计需要4步
     */
    public static Map<String, Integer> ladderLength(String beginWord, String endWord, List<String> wordList) {
        Map<String, Integer> marks = new HashMap<>();
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) {
            return marks;
        }
        marks.put(beginWord, 1);
//        marks.put(endWord, 0);
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);
        //图的广度优先遍历
        while (!queue.isEmpty()) {
            int currentSize = queue.size();
            //遍历当前层的每个节点
            for (int k = 0; k < currentSize; k++) {
                String word = queue.poll();
                int level = marks.get(word);
                char[] wordArray = word.toCharArray();
                for (int i = 0; i < word.length(); i++) {
                    char originChar = wordArray[i];
                    for (char j = 'a'; j <= 'z'; j++) {
                        if (wordArray[i] == j) continue;
                        wordArray[i] = j;
                        String check = new String(wordArray);
                        if (wordSet.contains(check) && check.equals(endWord)) {
                            marks.put(check, level + 1);
                            return marks;
                        }
                        if (wordSet.contains(check) && !marks.containsKey(check)) {
                            queue.add(check);
                            marks.put(check, level + 1);
                        }
                    }
                    wordArray[i] = originChar;
                }
            }

        }
        return marks;
    }

    public static int ladderLength2(String beginWord, String endWord, List<String> wordList) {
        // 第 1 步：先将 wordList 放到哈希表里，便于判断某个单词是否在 wordList 里
        Set<String> wordSet = new HashSet<>(wordList);
        if (wordSet.size() == 0 || !wordSet.contains(endWord)) {
            return 0;
        }
        wordSet.remove(beginWord);

        // 第 2 步：图的广度优先遍历，必须使用队列和表示是否访问过的 visited 哈希表
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);

        // 第 3 步：开始广度优先遍历，包含起点，因此初始化的时候步数为 1
        int step = 1;
        while (!queue.isEmpty()) {
            int currentSize = queue.size();
            for (int i = 0; i < currentSize; i++) {
                // 依次遍历当前队列中的单词
                String currentWord = queue.poll();
                // 如果 currentWord 能够修改 1 个字符与 endWord 相同，则返回 step + 1
                if (changeWordEveryOneLetter(currentWord, endWord, queue, visited, wordSet)) {
                    return step + 1;
                }
            }
            step++;
        }
        return 0;
    }

    /**
     * 尝试对 currentWord 修改每一个字符，看看是不是能与 endWord 匹配
     *
     * @param currentWord
     * @param endWord
     * @param queue
     * @param visited
     * @param wordSet
     * @return
     */
    private static boolean changeWordEveryOneLetter(String currentWord, String endWord,
                                                    Queue<String> queue, Set<String> visited, Set<String> wordSet) {
        char[] charArray = currentWord.toCharArray();
        for (int i = 0; i < endWord.length(); i++) {
            // 先保存，然后恢复
            char originChar = charArray[i];
            for (char k = 'a'; k <= 'z'; k++) {
                if (k == originChar) {
                    continue;
                }
                charArray[i] = k;
                String nextWord = String.valueOf(charArray);
                if (wordSet.contains(nextWord)) {
                    if (nextWord.equals(endWord)) {
                        return true;
                    }
                    if (!visited.contains(nextWord)) {
                        queue.add(nextWord);
                        // 注意：添加到队列以后，必须马上标记为已经访问
                        visited.add(nextWord);
                    }
                }
            }
            // 恢复
            charArray[i] = originChar;
        }
        return false;
    }

    public static void main(String[] args) {
        int steps2 = ladderLength2("hot", "dog", Arrays.asList("hot", "dog"));
        System.out.println(steps2);

        int steps = ladderLengthCalculate("hot", "dog", Arrays.asList("hot", "dog"));
        System.out.println(steps);

        int steps3 = ladderLength2("dog", "cat", Arrays.asList("dag", "cag", "cat", "cot"));
        System.out.println(steps3);

        int steps4 = ladderLengthCalculate("dog", "cat", Arrays.asList("dag", "cag", "cat", "cot"));
        System.out.println(steps4);

    }

    private static int ladderLengthCalculate(String hot, String dog, List<String> asList) {
        Map<String, Integer> result = ladderLength(hot, dog, asList);
        System.out.println(result);
        return result.get(dog) == null ? 0 : result.get(dog);
    }
}
