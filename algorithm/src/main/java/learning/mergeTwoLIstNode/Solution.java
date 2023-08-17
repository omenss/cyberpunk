package learning.mergeTwoLIstNode;

/**
 * @author lujun
 * @date 2023/7/31 10:37
 * @description 合并两个有序链表
 */
@SuppressWarnings("all")
public class Solution {


    /**
     * 合并两个单向列表 采用循环比较的方式
     *
     * @param list1 e.g [1,2,4]
     * @param list2 e.g [1,3,4]
     * @return e.g [1,1,2,3,4,4]
     * @description: 实现方案： 定义一个虚拟节点头dummy  然后比较p1 p2 的值
     * p1.val < p2.val 这时候dummy.next =p1 然后p1 =p1.next
     * p1.val>=p2.val 这时候dummy.next =p2 然后p2=p2.next
     * 每次做完一次比较  p 就像拉链一样往前移(p=p.next) 移动完之后再去 比较p1 p2的val 从中选取 最小的值 依次类推
     * 直到p1 或者p2 为null 为止 这时候 p1 或者p2可能只有其中一个不为null 那么p最后的next就指向那个不为null的节点
     * 也就是
     * if (p1 != null) {
     * p.next = p1;
     * }
     * if (p2 != null) {
     * p.next = p2;
     * }
     * 最后返回的数据就是dummy.next
     */
    public ListNode mergeTwoListsWithCirculate(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(), p = dummy;
        ListNode p1 = list1, p2 = list2;
        while (p1 != null && p2 != null) {
            if (p1.val < p2.val) {
                p.next = p1;
                p1 = p1.next;
            } else {
                p.next = p2;
                p2 = p2.next;
            }
            p = p.next;
        }
        if (p1 != null) {
            p.next = p1;
        }
        if (p2 != null) {
            p.next = p2;
        }
        return dummy.next;
    }

    /**
     * 合并两个单向列表 采用递归的方式
     *
     * @param list1 e.g [1,2,4]
     * @param list2 e.g [1,3,4]
     * @return e.g [1,1,2,3,4,4]
     * @description: 实现方案： 递归的方式就是不断的去比较两个链表的头节点的值
     */
    public ListNode mergeTwoListsWithRecursive(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        } else if (list2 == null) {
            return list1;
        } else if (list1.val < list2.val) {
            list1.next = mergeTwoListsWithRecursive(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoListsWithRecursive(list1, list2.next);
            return list2;
        }
    }



}
