package kiku.mutil;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.testng.AssertJUnit.assertTrue;

public class UtilSortTest {
    //helper
    public static int[] generateIncreasingRandoms(int amount, int min, int max) {
        Random rand = new Random();
        int[] res = new int[amount];
        for (int i = 0; i < res.length; i++) {
            res[i] = rand.nextInt(max - min + 1) + min;
        }
        Arrays.sort(res);
        return res;
    }

    public static void swap(int[] nums, int i, int j){
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    //##############################################################################################
    @Test
    public void testMedian() {
        int[] nums1 = generateIncreasingRandoms(100000, 1, 100000);
        int[] nums2 = generateIncreasingRandoms(100000, 1, 100000);;
        int[] nums3 = generateIncreasingRandoms(70001, 1, Integer.MAX_VALUE);;

        long startTimeBFS = System.currentTimeMillis();
        assertTrue(findMedianSortedArraysBFS(nums1, nums2) == findMedianSortedArraysBFS2(nums1, nums2));
        assertTrue(findMedianSortedArraysBFS(nums1, nums3) == findMedianSortedArraysBFS2(nums1, nums3));
        System.out.println((System.currentTimeMillis() - startTimeBFS) );

        long startTimeDFS = System.currentTimeMillis();
        assertTrue(findMedianSortedArraysDFS(nums1, nums2) == findMedianSortedArraysBFS2(nums1, nums2));
        assertTrue(findMedianSortedArraysDFS(nums1, nums3) == findMedianSortedArraysBFS2(nums1, nums3));
        System.out.println((System.currentTimeMillis() - startTimeDFS));

        long startTimeBF = System.currentTimeMillis();
        assertTrue(findMedianSortedArraysBruteForce(nums1, nums2) == findMedianSortedArraysBFS2(nums1, nums2));
        assertTrue(findMedianSortedArraysBruteForce(nums1, nums3) == findMedianSortedArraysBFS2(nums1, nums3));
        System.out.println((System.currentTimeMillis() - startTimeBF));

        System.out.println("Bruteforce : " + findMedianSortedArraysBruteForce(nums1, nums2));
        System.out.println("BFS : " + findMedianSortedArraysBFS2(nums1, nums2));
        System.out.println("DFS : " + findMedianSortedArraysDFS(nums1, nums2));
    }

    public double findMedianSortedArraysBruteForce(int[] nums1, int[] nums2) {
        int sum = nums1.length + nums2.length;
        int[] res = new int[sum];
        System.arraycopy(nums1, 0, res, 0, nums1.length);
        System.arraycopy(nums2, 0, res, nums1.length, nums2.length);
        Arrays.sort(res);

        if ( sum % 2 == 0) { //note index start with 0 so need to minus 1
            return (double) (res[sum/2 - 1]+ res[sum/2]) / 2;
        } else {
            return (double) res[sum/2];
        }
    }

    //median of 2 sorted array by Binary search:
    public double findMedianSortedArraysBinaySearch(int[] A, int[] B) {
        int m = A.length, n = B.length;
        int l = (m + n + 1) / 2;
        int r = (m + n + 2) / 2;
        return (getkthBS(A, 0, B, 0, l) + getkthBS(A, 0, B, 0, r)) / 2.0;
    }

    private double getkthBS(int[] A, int aStart, int[] B, int bStart, int k) {
        if (aStart > A.length - 1) return B[bStart + k - 1];
        if (bStart > B.length - 1) return A[aStart + k - 1];
        if (k == 1) return Math.min(A[aStart], B[bStart]);

        int aMid = Integer.MAX_VALUE, bMid = Integer.MAX_VALUE;
        if (aStart + k/2 - 1 < A.length) aMid = A[aStart + k/2 - 1];
        if (bStart + k/2 - 1 < B.length) bMid = B[bStart + k/2 - 1];

        if (aMid < bMid)
            return getkthBS(A, aStart + k/2, B, bStart,       k - k/2);// Check: aRight + bLeft
        else
            return getkthBS(A, aStart,       B, bStart + k/2, k - k/2);// Check: bRight + aLeft
    }

    public double findMedianSortedArraysBFS(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) return findMedianSortedArraysBFS(nums2, nums1);
        int sum = nums1.length + nums2.length;
        if ( sum % 2 == 0) {
            return (double) (findKthNumberBFS(nums1, nums2, sum / 2 + 1)
                    + findKthNumberBFS(nums1, nums2, sum / 2 )) / 2;
        } else {
            return findKthNumberBFS(nums1, nums2, sum / 2 + 1);
        }
    }

    public double findMedianSortedArraysBFS2(int[] nums1, int[] nums2) {
        int len = nums1.length + nums2.length;
        if ((len & 1) == 0) {
            return ((double) findKthNumberBFS2(nums1, nums2, (len >> 1))
                    + findKthNumberBFS2(nums1, nums2, (len >> 1) + 1)) / 2;
        } else {
            return findKthNumberBFS2(nums1, nums2, (len >> 1) + 1);
        }
    }

    public double findMedianSortedArraysDFS(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) return findMedianSortedArraysDFS(nums2, nums1);
        int sum = nums1.length + nums2.length;
        if ( sum % 2 == 0) {
            return (double) (findKthNumberDFS(nums1, nums2, sum / 2 + 1, 0, nums1.length, 0, nums2.length)
                    + findKthNumberDFS(nums1, nums2, sum / 2, 0, nums1.length, 0, nums2.length)) / 2;
        } else {
            return findKthNumberDFS(nums1, nums2, sum / 2 + 1, 0, nums1.length, 0, nums2.length);
        }
    }

    //DFS
    private int findKthNumberDFS(int[] nums1, int[] nums2, int k, int start1, int end1, int start2, int end2) {
        if (end1 - start1 > end2 - start2 ) {
            return findKthNumberDFS(nums2, nums1, k, start2, end2, start1, end1);
        } else if ( start1 == end1 ) { //running out of element in nums1
            return nums2[start2 + k - 1];
        } else if ( k == 1) {
            return Math.min(nums1[start1], nums2[start2]);
        }

        int mid1 = Math.min(k / 2, end1 - start1);
        int mid2 = k - mid1;
        if (nums1[start1 + mid1 - 1] > nums2[start2 + mid2 - 1]) {
            start2 += mid2;
            k -= mid2;
        } else if(nums1[start1 + mid1 - 1] < nums2[start2 + mid2 - 1]) {
            start1 += mid1;
            k -= mid1;
        } else {
            return nums1[start1 + mid1 - 1];
        }
        return findKthNumberDFS(nums1, nums2, k, start1, end1, start2, end2);
    }

    //BFS
    private int findKthNumberBFS(int[] nums1, int[] nums2, int k) {
        int start1 = 0, start2 = 0, end1 = nums1.length, end2 = nums2.length;
        //always maintain r1-l1 < r2-l2
        while ( k > 0 ) { //maintain the order, otherwise, index out of bound
            if ((end1 - start1) > (end2 - start2)) {
                //swap nums1, nums2
                int[] tmpArr = nums1;
                nums1 = nums2;
                nums2 = tmpArr;

                //swap start1, start2
                int tmpS = start1;
                start1 = start2;
                start2 = tmpS;

                //swap end1, end2
                int tmpE = end1;
                end1 = end2;
                end2= tmpE;
            } else if(start1 == end1) { //running out of element in nums1
                return nums2[start2 + k - 1];
            } else if ( k == 1 ) {
                return Math.min(nums1[start1], nums2[start2]);
            }
            int mid1 = Math.min(k / 2, end1 - start1);
            int mid2 = k - mid1;
            if (nums1[start1 + mid1 - 1] < nums2[start2 + mid2 - 1]) {
                start1 += mid1;
                k -= mid1;
            } else if (nums1[start1 + mid1 - 1] > nums2[start2 + mid2 - 1]){
                start2 += mid2;
                k -= mid2;
            } else { // nums1[start1 + mid1 - 1] == nums2[start2 + mid2 - 1]
                return nums1[start1 + mid1 - 1];
            }
        }
        return 0;
    }

    private int findKthNumberBFS2(int[] nums1, int[] nums2, int k) {
        int left1 = 0;
        int right1 = nums1.length;
        int left2 = 0;
        int right2 = nums2.length;
        while (k > 0) {
            if (right1 - left1 > right2 - left2) {
                int[] arr = nums1;
                nums1 = nums2;
                nums2 = arr;
                int tmp = left1;
                left1 = left2;
                left2 = tmp;
                tmp = right1;
                right1 = right2;
                right2 = tmp;
                continue;
            } else if (left1 == right1) {
                return nums2[left2 + k - 1];
            } else if (k == 1) {
                return Math.min(nums1[left1], nums2[left2]);
            }
            int mid1 = Math.min(k >> 1, right1 - left1);
            int mid2 = k - mid1;
            if (nums1[left1 + mid1 - 1] < nums2[left2 + mid2 - 1]) {
                left1 += mid1;
                k -= mid1;
            } else if (nums1[left1 + mid1 - 1] > nums2[left2 + mid2 - 1]) {
                left2 += mid2;
                k -= mid2;
            } else {
                return nums1[left1 + mid1 - 1];
            }
        }
        return 0;
    }

    //##############################################################################################
    @Test
    public void testTopFrequentK() {
        int[] nums1 = new int[]{1,1,1,2,2,3};
        int[] nums2 = generateIncreasingRandoms(10, 1, 5); //10 elems in nums, min 1, max 5

        // Note. list1.equals(list2) compares order
        // if don't care about order, put list to set and do set1.equals(set2)
        Set<Integer> s1 = new HashSet<>(topKFrequent(nums1, 2));
        Set<Integer> s2 = new HashSet<Integer>(Arrays.asList(new Integer[]{1,2}));
        assertTrue(s1.equals(s2));

        //System.out.println(topKFrequent(nums2, 3).toString());
        //System.out.println(topKFrequent2(nums2, 3).toString());
        assertTrue(topKFrequent(nums2, 3).equals(topKFrequent2(nums2, 3)));
    }

    /**
     * Bucket sort: count = index
     * Given a non-empty array of integers, return the k most frequent elements.
     */
    public List<Integer> topKFrequent(int[] nums, int k) {
        List<Integer> res = new ArrayList<>();
        List<Integer>[] bucket = new List[nums.length + 1]; //range 1 - k
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        for (int num : map.keySet()) {
            int idx = map.get(num);
            if (bucket[idx] == null) {
                bucket[idx] = new ArrayList<Integer>();
            }
            bucket[idx].add(num);
        }

        for (int i = bucket.length - 1; i >= 0 && k > 0; i--) {
            if (bucket[i] != null) {
                res.addAll(bucket[i]);
                k--;
            }
        }
        return res;
    }

    public List<Integer> topKFrequent2(int[] nums, int k) {

        List<Integer>[] bucket = new List[nums.length + 1];
        Map<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();

        for (int n : nums) {
            frequencyMap.put(n, frequencyMap.getOrDefault(n, 0) + 1);
        }

        for (int key : frequencyMap.keySet()) {
            int frequency = frequencyMap.get(key);
            if (bucket[frequency] == null) {
                bucket[frequency] = new ArrayList<>();
            }
            bucket[frequency].add(key);
        }

        List<Integer> res = new ArrayList<>();

        for (int pos = bucket.length - 1; pos >= 0 && res.size() < k; pos--) {
            if (bucket[pos] != null) {
                res.addAll(bucket[pos]);
            }
        }
        return res;
    }
    //##############################################################################################
    @Test
    public void testKthLargest() {
        int[] nums1 = new int[]{3,2,1,5,6,4};
        assertTrue(findKthLargestNumber(nums1, 2) == 5);
        assertTrue(quickSelectBFS(nums1, 2) == 5);

    }

    //[Quick select] Find the kth largest element in an unsorted array.
    public int findKthLargestNumber(int[] nums, int k) {
        return quickSelectDFS(nums, k, 0, nums.length - 1);
    }

    //QS-DFS
    public int quickSelectDFS(int[] nums, int k, int low, int high){
        if(low == high) return nums[high];
        int i = low , j = high;
        int middle = low+ (high-low)/2;
        int pivot = nums[middle];
        while (i<=j){
            while (nums[i] < pivot){ i ++;}
            while (nums[j] > pivot){ j --;}
            if(i<=j){
                swap(nums,i,j);
                i++;
                j--;
            }
        }
        int bigLen = high - i +1;

        if(bigLen >= k){
            return quickSelectDFS(nums, k, i, high);
        }else{
            return quickSelectDFS(nums, k-bigLen, low, i-1);
        }
    }

    //QS-BFS
    public int quickSelectBFS(int[] nums, int k) {
        int start = 0, end = nums.length - 1;
        k = nums.length - k;
        shuffle(nums);

        while( start <= end) {
            int i = start, j = end;
            while (i < j) { //let pivot = end
                while (i < j && nums[j] >= end) j--;
                while (i < j && nums[i] < end) i++;
                if( i ==j ) break;
                swap(nums, i, j);
            }
            swap(nums, i, end); //now i == j
            if ( i < k ) {
                start = i + 1;
            } else if (i > k) {
                end = i - 1;
            } else {
                return nums[i];
            }
        }
        return 0;
    }

    private void shuffle(int[] nums) {
        java.util.Random rand = new java.util.Random(); //not use ifwe.random()
        for (int i = nums.length - 1; i >= 0; i--) {
            swap2(nums, i, rand.nextInt(i + 1)); // return int < i + 1
        }
    }

    private void swap2(int[] nums, int i, int j){
        int s = nums[i]+nums[j];
        nums[i] = s-nums[i];
        nums[j] = s-nums[j];
    }

    //##############################################################################################
    //idea of MergeSort
    /**
     * Definition for singly-linked list.
     */
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
    //##############################################################################################
    //Try1 :given an array of list node and sort them
    public static ListNode mergeKLists(ListNode[] lists){
        return partition(lists,0,lists.length-1);
    }

    public static ListNode partition(ListNode[] lists, int s, int e){
        if (s > e) return null;
        if (s == e) return lists[s];
        int mid = s + (e - s) / 2;
        ListNode l1 = partition(lists, s, mid);
        ListNode l2 = partition(lists, mid + 1, e);
        return merge(l1, l2);
    }

    //This function is from Merge Two Sorted Lists.
    public static ListNode merge(ListNode l1,ListNode l2){
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val < l2.val) {
            l1.next = merge(l1.next, l2);
            return l1;
        } else {
            l2.next = merge(l2.next, l1);
            return l2;
        }
    }

    //##############################################################################################
    //Given a list node, and sort the list
    public ListNode sortListAsc(ListNode head) {
        if (head == null) return null;
        if (head.next == null) return head;

        ListNode fast = head, slow = head, mid = head;
        while(fast != null && fast.next != null) {
            mid = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        mid.next = null;
        ListNode h1 = sortListAsc(head);
        ListNode h2 = sortListAsc(slow);
        return mergeAsc(h1, h2);
    }

    public ListNode mergeAsc(ListNode h1, ListNode h2) {
        if (h1 == null && h2 == null) return null;
        if (h1 == null) return h2;
        if (h2 == null) return h1;
        if (h1.val < h2.val) {
            h1.next = mergeAsc(h1.next, h2);
            return h1;
        } else {
            h2.next = mergeAsc(h1, h2.next);
            return h2;
        }
    }

    //Try3
    public ListNode sortList2(ListNode head) {
        if (head == null) return head;
        if (head.next == null) return head;

        //p1 move 1 step every time, p2 move 2 step every time, pre record node before p1
        ListNode p1 = head;
        ListNode p2 = head;
        ListNode pre = head;

        while(p2 != null && p2.next != null) {
            pre = p1;
            p1 = p1.next;
            p2 = p2.next.next;
        }

        //change pre next to null, make two sub list(head to pre, p1 to p2)
        pre.next = null;
        ListNode h1 = sortList2(head);
        ListNode h2 = sortList2(p1);
        return merge2(h1, h2);
    }

    public ListNode merge2(ListNode h1, ListNode h2) {
        if (h1 == null) return h2;
        if (h2 == null) return h1;

        if (h1.val < h2.val) {
            h1.next = merge2(h1.next, h2);
            return h1;
        }else {
            h2.next = merge2(h1, h2.next);
            return h2;
        }
    }
    //##############################################################################################
    @Test
    public void testFreedomTrail() {
        FreedomTrail ft = new FreedomTrail();
        String ring = "godding";
        String key = "gd";
        assertTrue(ft.findRotateStepsDFS(ring, key) == 4);
        assertTrue(ft.findRotateStepsDFS(ring, key) == ft.findRotateStepsDFS(ring, key));
    }

    class FreedomTrail {
        Map<String, Map<Integer, Integer>> memo;

        public int findRotateStepsDFS(String ring, String key) {
            memo = new HashMap<>();
            return dfs(ring, key, 0);
        }

        private int findPos(String ring, char ch) { // find first occurrence clockwise
            return ring.indexOf(ch);
        }

        private int findBackPos(String ring, char ch) { //find first occurrence  anti-clockwise
            if (ring.charAt(0) == ch) return 0;
            for (int i = ring.length() - 1; i > 0; i--) {
                if (ring.charAt(i) == ch) return i;
            }
            return 0;
        }

        private int dfs(String ring, String key, int i) {
            if (i == key.length()) return 0;
            int res = 0;
            char ch = key.charAt(i);
            if (memo.containsKey(ring) && memo.get(ring).containsKey(i))
                return memo.get(ring).get(i);
            int f = findPos(ring, ch);
            int b = findBackPos(ring, ch);
            int forward = 1 + f + dfs(ring.substring(f) + ring.substring(0, f), key, i + 1);
            int back = 1 + ring.length() - b + dfs(ring.substring(b) + ring.substring(0, b), key, i + 1);
            res = Math.min(forward, back);
            Map<Integer, Integer> ans = memo.getOrDefault(ring, new HashMap<>());
            ans.put(i, res);
            memo.put(ring, ans);
            return res;
        }

        //DP
        public int findRotateStepsDP(String ring, String key) {
            int n = ring.length();
            int m = key.length();
            int[][] dp = new int[m + 1][n];

            for (int i = m - 1; i >= 0; i--) {
                for (int j = 0; j < n; j++) {
                    dp[i][j] = Integer.MAX_VALUE;
                    for (int k = 0; k < n; k++) {
                        if (ring.charAt(k) == key.charAt(i)) {
                            int diff = Math.abs(j - k);
                            int step = Math.min(diff, n - diff);
                            dp[i][j] = Math.min(dp[i][j], step + dp[i + 1][k]);
                        }
                    }
                }
            }
            return dp[0][0] + m;
        }
    }
    //##############################################################################################

}

