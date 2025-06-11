package com.feng.algos;

/**
 * @author fengsy
 * @date 6/13/21
 * @Description
 */
public class Prime {

    static void init_prime(int[] primes) {

        // 素数筛的标记过程
        for (int i = 2; i * i <= 10; i++) {
            if (primes[i] == 1) {
                continue;
            }
            // 用 j 枚举所有素数 i 的倍数
            for (int j = 2 * i; j <= 10; j += i) {
                primes[j] = 1; // 将 j 标记为合数
            }
        }
        for (int i = 2; i < primes.length; i++) {
            if (primes[i] == 0) {
                System.out.println(i);
            }
        }
        return;
    }

    public static void main(String[] args) {
        int[] primes = new int[15];
        init_prime(primes);
        int sum = 0;
        for (int i = 2; i <= 10; i++) {
            sum += i * (1 - primes[i]); // 素数累加
        }
        System.out.println("sum: " + sum);
    }
}
