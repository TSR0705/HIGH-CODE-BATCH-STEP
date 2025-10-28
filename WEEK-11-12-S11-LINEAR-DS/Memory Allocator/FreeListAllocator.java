import java.util.*;

class FreeListAllocator {
    private static class Node {
        int start, size;
        Node next;
        Node(int start, int size) {
            this.start = start;
            this.size = size;
        }
    }

    private Node head;
    private final int heapSize;

    public FreeListAllocator(int heapSize) {
        this.heapSize = heapSize;
        this.head = new Node(0, heapSize);
    }

    public int malloc(int size) {
        Node prev = null, curr = head;

        while (curr != null) {
            if (curr.size >= size) {
                int allocStart = curr.start;

                if (curr.size == size) {
                    if (prev == null) head = curr.next;
                    else prev.next = curr.next;
                }
                else {
                    curr.start += size;
                    curr.size -= size;
                }

                return allocStart;
            }
            prev = curr;
            curr = curr.next;
        }

        return -1;
    }

    public void free(int ptr, int size) {
        Node newNode = new Node(ptr, size);
        if (head == null || ptr < head.start) {
            newNode.next = head;
            head = newNode;
        } else {
            Node curr = head;
            while (curr.next != null && curr.next.start < ptr) {
                curr = curr.next;
            }
            newNode.next = curr.next;
            curr.next = newNode;
        }

        coalesce();
    }

    private void coalesce() {
        Node curr = head;
        while (curr != null && curr.next != null) {
            if (curr.start + curr.size == curr.next.start) {
                curr.size += curr.next.size;
                curr.next = curr.next.next;
            } else {
                curr = curr.next;
            }
        }
    }

    public List<int[]> freeList() {
        List<int[]> res = new ArrayList<>();
        Node curr = head;
        while (curr != null) {
            res.add(new int[]{curr.start, curr.size});
            curr = curr.next;
        }
        return res;
    }

    public void printFreeList() {
        System.out.print("Free: ");
        for (int[] b : freeList()) {
            System.out.print("[" + b[0] + "," + b[1] + "] ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        FreeListAllocator alloc = new FreeListAllocator(20);

        int a = alloc.malloc(8);
        System.out.println("malloc(8) -> " + a);
        alloc.printFreeList();

        int b = alloc.malloc(4);
        System.out.println("malloc(4) -> " + b);
        alloc.printFreeList();

        alloc.free(a, 8);
        System.out.println("free(0,8)");
        alloc.printFreeList();

        int c = alloc.malloc(6);
        System.out.println("malloc(6) -> " + c);
        alloc.printFreeList();

        alloc.free(b, 4);
        System.out.println("free(8,4)");
        alloc.printFreeList();

        alloc.free(12, 8);
        System.out.println("free(12,8)");
        alloc.printFreeList();
    }
}
