package com.tsystems.javaschool.tasks.pyramid;

import java.util.*;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        if (inputNumbers.size() < 3 || inputNumbers.size() > 1000) throw new CannotBuildPyramidException();
        if (inputNumbers.contains(null)) throw new CannotBuildPyramidException();
        inputNumbers.sort((o1, o2) -> o1.compareTo(o2));

        int elCnt = inputNumbers.size();
        int rows = 1;
        int cnt = 1;
        while (true) {
            elCnt -= cnt;
            if (elCnt == 0) break;
            if (elCnt < 0) throw new CannotBuildPyramidException();
            rows++;
            cnt += 1;
        }
        int columns = (cnt * 2) - 1;
        int[][] arr = new int[rows][columns];

        int pos = 0;
        int number = 0;
        for (int i = 0; i < rows; i++) {
            int curPos = pos;
            int offset = 0;
            for (int j = 0; j < i + 1; j++) {
                arr[i][columns / 2 - curPos + offset] = inputNumbers.get(number);
                offset += 2;
                number++;
            }
            pos++;
        }
        return arr;
    }

}
