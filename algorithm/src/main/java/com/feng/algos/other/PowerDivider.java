package com.feng.algos.other;

/**
 * @Description leecode 279. 完全平方数
 * @Author fengsy
 * @Date 12/7/21
 */
public class PowerDivider {
    /**
     * 动态规划
     *
     * @param n
     * @return
     */
    public static int numSquares(int n) {
        int[] f = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int minn = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                minn = Math.min(minn, f[i - j * j]);
            }
            f[i] = minn + 1;
        }
        return f[n];
    }

    /**
     * 四平方和定理
     * 四平方和定理证明了任意一个正整数都可以被表示为至多四个正整数的平方和。这给出了本题的答案的上界。
     * 同时四平方和定理包含了一个更强的结论：当且仅当 n != 4^k*(8m+7)时，n可以被表示为至多三个正整数的平方和。
     * 因此，当 n = 4^k*(8m+7)时，n 只能被表示为四个正整数的平方和。此时我们可以直接返回 4。
     * 当 n != 4^k*(8m+7)时，我们需要判断到底多少个完全平方数能够表示n，我们知道答案只会是1,2,3中的一个：
     * 答案为 1 时，则必有 n 为完全平方数，这很好判断；
     * 答案为 2 时，则有 n=a^2+b^2,我们只需要枚举所有的 a(1 <= a <= sqrt{n})，判断 n-a^2是否为完全平方数即可；
     * 答案为 3 时，我们很难在一个优秀的时间复杂度内解决它，但我们只需要检查答案为 1 或 2 的两种情况，即可利用排除法确定答案。
     *
     * @param n
     * @return
     */
    public static int numSquares2(int n) {
        if (isPerfectSquare(n)) {
            return 1;
        }
        if (checkAnswer4(n)) {
            return 4;
        }
        for (int i = 1; i * i <= n; i++) {
            int j = n - i * i;
            if (isPerfectSquare(j)) {
                return 2;
            }
        }
        return 3;
    }

    // 判断是否为完全平方数
    private static boolean isPerfectSquare(int x) {
        int y = (int) Math.sqrt(x);
        return y * y == x;
    }

    // 判断是否能表示为 x=4^k*(8m+7)
    private static boolean checkAnswer4(int x) {
        while (x % 4 == 0) {
            x /= 4;
        }
        return x % 8 == 7;
    }

    public static void main(String[] args) {
        System.out.println(numSquares(12));
        System.out.println(numSquares2(12));
    }

}
