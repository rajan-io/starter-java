package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.example.AllOne.FreqNode.newNodeBetween;

public class AllOne {

    public static class FreqNode {
        int freq;
        Set<String> keySet = new HashSet<>();
        FreqNode prev;
        FreqNode next;

        FreqNode(int freq) {
            this.freq = freq;
        }

        public static FreqNode newNodeBetween(String key, int freq, FreqNode prevNode, FreqNode nextNode) {
            FreqNode newNode = new FreqNode(freq);
            newNode.next = nextNode;
            newNode.prev = prevNode;

            nextNode.prev = newNode;
            prevNode.next = newNode;

            newNode.keySet.add(key);
            return newNode;
        }
    }

    private FreqNode head;
    private FreqNode tail;
    private Map<String, FreqNode> freqBucketLookup;

    public AllOne() {
        freqBucketLookup = new HashMap<>();
        head = new FreqNode(0);
        tail = new FreqNode(0);
        head.next = tail;
        tail.prev = head;
    }
    public void inc(String key) {
        if (freqBucketLookup.containsKey(key)) { // bucket exits
            FreqNode cureNode = freqBucketLookup.get(key);
            int newFreq = cureNode.freq + 1;
            moveToNextIncBucket(key, cureNode, newFreq);
        } else { // new bucket for freq 1
            moveToNextIncBucket(key, head, 1);
        }
    }

    public void dec(String key) {
        if (!freqBucketLookup.containsKey(key)) {
            return;
        }
        FreqNode curNode = freqBucketLookup.get(key);
        int newFreq = curNode.freq - 1;
        if (newFreq == 0) { // reduced to 0
            freqBucketLookup.remove(key);
        } else { // dec to lower bucket
            moveToDecFrequency(key, curNode, newFreq);
        }

        if (curNode.keySet.isEmpty()) {
            removeEmptyNode(curNode);
        }
    }

    private void moveToDecFrequency(String key, FreqNode curNode, int newFreq) {
        FreqNode prevNode = curNode.prev;
        if (invalidPrevBucket(prevNode, newFreq)) {
            FreqNode newNode = newNodeBetween(key, newFreq, prevNode, curNode);
            curNode.keySet.remove(key);
            freqBucketLookup.put(key, newNode);
        } else {
            curNode.keySet.remove(key);
            freqBucketLookup.put(key, prevNode);
        }
    }

    private boolean invalidPrevBucket(FreqNode prevNode, int newFreq) {
        return prevNode == head || prevNode.freq != newFreq;
    }

    private static void removeEmptyNode(FreqNode node) {
        FreqNode prevNode = node.prev;
        FreqNode nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }



    private void moveToNextIncBucket(String key, FreqNode cureNode, int newFreq) {
        FreqNode nextNode = cureNode.next;
        if (invalidNextBucket(nextNode, newFreq)) { // create node insert next freq
            FreqNode newNode = newNodeBetween(key, newFreq, cureNode, nextNode);
            freqBucketLookup.put(key, newNode);
        } else {  // move key to next freq bucket
            nextNode.keySet.add(key);
            freqBucketLookup.put(key, nextNode);
        }
    }

    private boolean invalidNextBucket(FreqNode nextNode, int newFreq) {
        return nextNode == tail || nextNode.freq != newFreq;
    }

    public String getMaxKey() {
        return tail.prev == head ? "" : tail.prev.keySet.iterator().next();
    }

    public String getMinKey() {
        return head.next == tail ? "" : head.next.keySet.iterator().next();
    }
}
