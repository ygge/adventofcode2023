package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiDimPos {
    public int[] dim;

    public MultiDimPos(int... values) {
        dim = new int[values.length];
        System.arraycopy(values, 0, dim, 0, dim.length);
    }

    public List<MultiDimPos> neighbours() {
        List<MultiDimPos> ans = new ArrayList<>();
        var data = new int[dim.length];
        calcNeigh(data, ans, 0, true);
        return ans;
    }

    private void calcNeigh(int[] data, List<MultiDimPos> ans, int index, boolean allZero) {
        if (index == data.length) {
            if (!allZero) {
                ans.add(new MultiDimPos(data));
            }
        } else {
            for (int i = -1; i <= 1; ++i) {
                var newData = new int[data.length];
                System.arraycopy(data, 0, newData, 0, index);
                newData[index] = dim[index] + i;
                calcNeigh(newData, ans, index + 1, allZero && i == 0);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiDimPos that = (MultiDimPos) o;
        return Arrays.equals(dim, that.dim);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dim);
    }

    @Override
    public String toString() {
        return "MultiDimPos{" +
                "dim=" + Arrays.toString(dim) +
                '}';
    }
}
