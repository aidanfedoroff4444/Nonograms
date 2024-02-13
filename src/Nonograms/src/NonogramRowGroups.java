package Nonograms.src;

import java.util.ArrayList;

public class NonogramRowGroups {
    public ArrayList<Integer> groupLengths;
    public ArrayList<Integer> groupSymbols;

    public NonogramRowGroups() {
        groupLengths = new ArrayList<>();
        groupSymbols = new ArrayList<>();
    }

    public void addGroup(int length, int symbol) {
        groupLengths.add(length);
        groupSymbols.add(symbol);
    }

    public ArrayList<Integer> getGroupLengths() { return groupLengths; }
    public ArrayList<Integer> getGroupSymbols() { return groupSymbols; }
}
