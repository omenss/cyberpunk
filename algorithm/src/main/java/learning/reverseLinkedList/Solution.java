package learning.reverseLinkedList;

/**
 * @author lujun
 * @date 2023/8/15 09:45
 * @description: Given the head of a singly linked list, reverse the list, and return the reversed list.
 * <p>
 * Example 1:
 * <p>
 * <p>
 * Input: head = [1,2,3,4,5]
 * Output: [5,4,3,2,1]
 * Example 2:
 * <p>
 * <p>
 * Input: head = [1,2]
 * Output: [2,1]
 * Example 3:
 * <p>
 * Input: head = []
 * Output: []
 * Constraints:
 * <p>
 * The number of nodes in the list is the range [0, 5000].
 * -5000 <= Node.val <= 5000
 * Follow up: A linked list can be reversed either iteratively or recursively. Could you implement both?
 */
public class Solution {


    /**
     * 循环遍历方式实现
     *
     * @param head 链表头节点
     * @return 反转后的链表头节点
     * @description :
     * 先提前定义一个 prev
     * 再定义一个 curr=head 然后依次从头开始遍历 curr
     * ListNode next =curr.next
     * 每次遍历都将 curr.next 指向 prev
     * <p>
     * 最后 curr = next
     * <p>
     * 1->2->3
     * <p>
     * null 1->2->3
     * <p>
     * null<-1 2->3
     * <p>
     * 1<-2 3
     * <p>
     * 1<-2<-3
     */
    public ListNode reverseListWithIteratively(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return head;
    }
}
