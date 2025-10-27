import java.util.*;

class ListNode {
    int val;
    ListNode next;
    ListNode(int v) { val = v; }
}

public class PlaylistManager {

    private ListNode head = null;
    private ListNode tail = null;

    // Main runner: executes commands and returns final playlist
    public String runPlaylist(List<String> commands) {
        for (String cmd : commands) {
            String[] parts = cmd.trim().split(" ");
            String action = parts[0];

            switch (action) {
                case "ADD_END":
                    addEnd(Integer.parseInt(parts[1]));
                    break;
                case "ADD_AFTER":
                    addAfter(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    break;
                case "DELETE":
                    delete(Integer.parseInt(parts[1]));
                    break;
                case "REVERSE_K":
                    reverseK(Integer.parseInt(parts[1]));
                    break;
                case "DEDUP":
                    dedup();
                    break;
                case "PRINT":
                    return printList();
            }
        }
        // If PRINT never appears, return final playlist
        return printList();
    }

    private void addEnd(int x) {
        ListNode node = new ListNode(x);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
    }

    private void addAfter(int a, int b) {
        ListNode curr = head;
        while (curr != null) {
            if (curr.val == a) {
                ListNode node = new ListNode(b);
                node.next = curr.next;
                curr.next = node;
                if (curr == tail) tail = node;
                return;
            }
            curr = curr.next;
        }
    }

    private void delete(int x) {
        if (head == null) return;

        if (head.val == x) {
            head = head.next;
            if (head == null) tail = null;
            return;
        }

        ListNode prev = head;
        ListNode curr = head.next;
        while (curr != null) {
            if (curr.val == x) {
                prev.next = curr.next;
                if (curr == tail) tail = prev;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    private void reverseK(int k) {
        head = reverseKGroup(head, k);
        tail = head;
        if (tail != null) while (tail.next != null) tail = tail.next;
    }

    private ListNode reverseKGroup(ListNode head, int k) {
        ListNode curr = head;
        int count = 0;
        while (curr != null && count < k) {
            curr = curr.next;
            count++;
        }
        if (count < k) return head; // Less than k nodes — don't reverse

        curr = head;
        ListNode prev = null, next = null;
        count = 0;
        while (curr != null && count < k) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
            count++;
        }

        if (next != null)
            head.next = reverseKGroup(next, k);

        return prev;
    }

    private void dedup() {
        ListNode outer = head;
        while (outer != null) {
            ListNode inner = outer;
            while (inner.next != null) {
                if (inner.next.val == outer.val) {
                    inner.next = inner.next.next;
                } else {
                    inner = inner.next;
                }
            }
            outer = outer.next;
        }
        tail = head;
        if (tail != null) while (tail.next != null) tail = tail.next;
    }

    private String printList() {
        StringBuilder sb = new StringBuilder();
        ListNode curr = head;
        while (curr != null) {
            sb.append(curr.val);
            if (curr.next != null) sb.append(" ");
            curr = curr.next;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = Integer.parseInt(sc.nextLine());
        List<String> cmds = new ArrayList<>();
        for (int i = 0; i < m; i++) cmds.add(sc.nextLine());

        PlaylistManager pm = new PlaylistManager();
        System.out.println(pm.runPlaylist(cmds));
    }
}
