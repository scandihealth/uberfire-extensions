package org.uberfire.ext.editor.commons.client.history;

import java.util.*;

// This class helps decorate the graphSlice element in ExtendedVersionRecords.
// Based on the entire history of versions, it will calculate a history tree
// from the initially searched rule and all it's ancestors.
// Each time a rule is renamed or copied this will be depicted by a new 'branch'.
// VersionHistoryPresenter will collect the history for each rule in the ancestry line
// and call addBranch once for each new set of versions, as they are added to the global
// list of version records. Ultimately all these versions are sorted by date, and decorate()
// is called.
// Since each version represents a date, it will check which branches are starting, ongoing
// or terminating in that row and add a vertical, coloured line accordingly.
// The actual version for each specific row (there is always exactly one) is depicted by
// a circle at the relevant branch.
// Furthermore, a new branch starting as the offspring of an ancestor branch will be designated
// by a diagonal line to illustrate the heritage.
// All graphic elements are generated using plain html/css and placed as a normal property in the
// ExtendedVersionRecord to be rendered in a GWT table.

public class ExtendedVersionGraph {

    // Helper pojo for keeping track of each rule/branch. Offset is 0 for the initial branch (rendered right-most),
    // 1 for its immediate ancestor  (rendered one position left of the first branch) etc.
    static class Branch {
        public int offset;
        public Date firstDate;
        public Date lastDate;
        public String color;
    }

    // Cycle through these colours for each branch. Initial branch is always black.
    final static String[] cols = { "black", "green", "blue", "red", "yellow", "orange"};
    Date previousBranchFirstDate = new Date(0);

    Map<String, Branch> branches = new HashMap<String, Branch>();

    boolean branchHasAncestor(Branch b) {
        // They all do, except the branch that was added last. It is the initial forefather.
        return b.offset < branches.size() - 1;
    }

    public void addBranch(String name, Date first, Date last) {
        // Even if a branch has no newer versions after a specific date, we may still want to render
        // it for the following rows until we reach the offspring-point of it child branch (added previously).
        // We use previousBranchFirstDate to keep track of this.
        if (last.compareTo(previousBranchFirstDate) < 0) last = previousBranchFirstDate;
        Branch branch = new Branch();
        branch.offset = branches.size();
        branch.firstDate = first;
        branch.lastDate = last;
        branch.color = cols[branch.offset % cols.length];
        previousBranchFirstDate = first;
        branches.put(name, branch);
    }

    static int compareTo(Date d1, String n1, Date d2, String n2) {
        int v = d1.compareTo(d2);
        if (v != 0) return v;
        return n1.compareTo(n2); // if dates are identical, use name as second sorting criteria to get deterministic ordering
    }

    // To be called after all version records have been added, all branches have been registered, and
    // all records have been sorted by date.
    public void decorate(List<ExtendedVersionRecord> records) {
        for (ExtendedVersionRecord r : records) {
            try {
                Branch b = branches.get(r.name());
                String graph = "<div style='position:relative; width:100%; height:28px; overflow:visible;'>"; // wrapper to fit table cell
                int addStub = -1;
                if (r.date().compareTo(b.firstDate) == 0 && branchHasAncestor(b)) {
                    // Add diagonal line to indicate that this branch is an offspring from ancestor on the left
                    graph += addDiagonal(b.offset, branches.size(), b.color);
                    addStub = b.offset + 1; // Extend ancestor branch line just a tad to match up with diagonal
                }
                for (String name: branches.keySet()) {
                    Branch t = branches.get(name);
                    // For all branches, add vertical lines as long as they are ongoing (given the date for the current row/version)
                    int first = compareTo(t.firstDate, name, r.date(), r.name());
                    int last = compareTo(t.lastDate, name, r.date(), r.name());
                    if (first < 0 && last > 0) graph += addLine(t.offset, branches.size(), t.color);
                    else if (first < 0 && last == 0) graph += addUpper(t.offset, branches.size(), t.color);
                    else if (first == 0 && last > 0) graph += addLower(t.offset, branches.size(), t.color);
                    else if (addStub == t.offset) graph += addUpper(t.offset, branches.size(), t.color);
                }
                // Add a circle to indicate the committed version belonging to this row
                graph += addCircle(b.offset, branches.size(), b.color);
                graph += "</div>"; // end wrapper
                r.setGraphSlice(graph);
            } catch (Exception e) {
                r.setGraphSlice("<div>no graph available</div>");
            }
        }
    }

    // Helpers for generating the html/css graphic primitives. (Note: Could probably be nicer with newer CSS features, SVG or HTML5, but browser compatibility is treasured here.)

    static String addCircle(int offset, int num, String col) {
        float p = 100.0f * (1.0f - (offset + 0.5f) / num);
        return "<div style='position:absolute; background-color:" + col + "; height:15px; width:15px; border-radius:50%; left:" + p + "%; top: 50%; transform: translate(-50%,-50%);'></div>";
    }

    static String addDiagonal(int offset, int num, String col) {
        float p = 100.0f * (1.0f - (offset + 1.5f) / num);
        return "<div style='position:relative; overflow:visible; width:" + (100.0f / num) + "%; height:50%; left: " + p + "%; top: 0%;'>" +
                "<div style='position:absolute; height:4px; width:100%; background-color:" + col + "; left:0%; top:40%; transform: rotate(15deg);'></div>" +
                "</div>";
    }

    static String addUpper(int offset, int num, String col) {
        float p = 100.0f * (1.0f - (offset + 0.5f) / num);
        return "<div style='position:absolute; background-color:" + col + "; height:40%; width:4px; left:" + p + "%; top: 0%; transform: translate(-50%,-45%);'></div>";
    }

    static String addLower(int offset, int num, String col) {
        float p = 100.0f * (1.0f - (offset + 0.5f) / num);
        return "<div style='position:absolute; background-color:" + col + "; height:60%; width:4px; left:" + p + "%; top: 50%; transform: translate(-50%,10%);'></div>";
    }

    static String addLine(int offset, int num, String col) {
        float p = 100.0f * (1.0f - (offset + 0.5f) / num);
        return "<div style='position:absolute; background-color:" + col + "; height:120%; width:4px; left:" + p + "%; top: 0%; transform: translate(-50%,-10%);'></div>";
    }
}
