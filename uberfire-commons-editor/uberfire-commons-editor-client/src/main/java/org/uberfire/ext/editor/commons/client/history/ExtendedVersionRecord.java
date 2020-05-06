package org.uberfire.ext.editor.commons.client.history;

import org.uberfire.java.nio.base.version.VersionRecord;

// An extended version record need to be able to display a version tree for related rules in one table
public interface ExtendedVersionRecord extends VersionRecord, Comparable<ExtendedVersionRecord> {
    String versionNumber();
    String name();
    String graphSlice();
    boolean isCurrentRule();
    void setGraphSlice(String graph);
}
