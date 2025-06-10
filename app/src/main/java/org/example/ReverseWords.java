package org.example;

import java.util.Arrays;
import java.util.List;

public class ReverseWords {
    public String reverseWordsBuiltIn(String input) {
        List<String> wordsReversed = Arrays.stream(input.split(" "))
                .filter(w -> !w.isBlank())
                .toList()
                .reversed();

        return String.join(" ", wordsReversed);
    }
    public String reverseWords(String input){
        char[] buffer = input.toCharArray();
        //reverse all words
        reverseInplace(buffer, 0, buffer.length-1);

        //reverse each word
        int start = 0, end = 0;
        int len = buffer.length;
        while (start < len) {
            //find word end
            while (end < len && buffer[end] != ' ') ++end;
            reverseInplace(buffer, start, end-1);
            start = end +1;
            ++end;
        }

        int endIndex = removeExtraSpaces(buffer);

        return new String(buffer, 0, endIndex+1);
    }

    private static int removeExtraSpaces(char[] buffer) {
        int i = 0, collectIdx = 0;
        char preChar = ' ';

        while (i < buffer.length) {
            if( (preChar == ' ')&& buffer[i] == ' ') {//ignore extra space
                preChar = buffer[i];
                i++;
            } else { // collect character at first pointer
                preChar = buffer[i];
                buffer[collectIdx] = buffer[i];
                collectIdx++;
                i++;
            }
        }
        //remove trailing space for last
        int endIndex = collectIdx -1;
        while(buffer[endIndex] == ' ') {
            endIndex--;
        }
        return endIndex;
    }


    private void reverseInplace(char[] buffer, int start, int end) {
        int left = start, right = end;
        while(left < right) {
            //swap
            char lValue = buffer[left];
            char rValue = buffer[right];
            buffer[left] = rValue;
            buffer[right] = lValue;

            left++;
            right--;

        }
    }
}
