package learning.lengthOfLongestSubstring;

import java.util.HashMap;

/**
 * @author lujun
 * @date 2023/8/4 13:49
 * @description :
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 * <p>
 * <p>
 * 示例 1:
 * <p>
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 * <p>
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 * <p>
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 * 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 * <p>
 * <p>
 * 提示：
 * <p>
 * 0 <= s.length <= 5 * 104
 * s 由英文字母、数字、符号和空格组成
 * @link https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 */
public class Solution {

    /**
     * 滑动窗口模式
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        if (s.length() <= 1) {
            return s.length();
        }
        HashMap<Character, Integer> window = new HashMap<>();
        int left = 0, right = 0;
        for (int i = 0; i < s.length(); i++) {
            if (window.containsKey(s.charAt(i))) {
                left = Math.max(left, window.get(s.charAt(i)) + 1);
            }
            window.put(s.charAt(i), i);
            right = Math.max(right, i - left + 1);
        }
        return right;
    }

}
