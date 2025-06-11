package com.feng.algos.string;

/**
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class StrToInt {
    public static int myAtoi(String s) {
        int i = 0; // i iterates through the chars in s
        int output = 0; // stores the result

        // 1. Read in whitespace
        while (i < s.length() && s.charAt(i) == ' ') {
            i++;
        }

        // 2. Read in optional sign
        boolean isNegative = false;
        if (i < s.length()) {
            if (s.charAt(i) == '-') {
                isNegative = true;
                i++;
            } else if (s.charAt(i) == '+') {
                i++;
            }
        }

        // Keep processing as long as there are digits left
        while (i < s.length() && "0123456789".indexOf(s.charAt(i)) > -1) {
            // 3. Read in digit
            int digit = Character.getNumericValue(s.charAt(i));

            // 4. Convert digit to integer
            int oldOutput = output;
            output = output * 10 + digit;

            // 5. Detect overflow
            // Note: Overflow can happen from multiplication or addition. Addition
            // overflow is checked by testing if output < 0; multiplication overflow
            // is checked by undoing the operation and testing the result.
            if (output < 0 || (output - digit) / 10 != oldOutput) {
                return isNegative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }

            i++;
        }

        // 6. Return final result
        return isNegative ? -output : output;
    }

    public static void main(String[] args) {
        System.out.println(myAtoi("   -224344asf"));
    }
}
