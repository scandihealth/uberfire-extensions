package org.uberfire.ext.editor.commons.client.history;

import java.util.Date;

public class ExtendedVersionRecordImpl implements ExtendedVersionRecord {

    private String _name;
    private String _versionNumber;
    private String _graphSlice; // Note: Will contain raw HTML to render a horizontal slice of the version graph
    private String _id;
    private String _author;
    private String _email;
    private String _comment;
    private String _uri;
    private Date _date;
    private boolean _isCurrentRule;

    public ExtendedVersionRecordImpl(
            String name,
            String versionNumber,
            String graphSlice,
            String id,
            String author,
            String email,
            String comment,
            String uri,
            Date date,
            boolean isCurrentRule) {
        _name = name;
        _versionNumber = versionNumber;
        _graphSlice = graphSlice;
        _id = id;
        _author = author;
        _email = email;
        _comment = comment;
        _uri = uri;
        _date = date;
        _isCurrentRule = isCurrentRule;
    }

    @Override
    public boolean isCurrentRule() { return _isCurrentRule; }

    @Override
    public void setGraphSlice(String graph) {
        _graphSlice = graph;
    }

    @Override
    public String name() { return _name; }

    @Override
    public String versionNumber() { return _versionNumber; }

    @Override
    public String graphSlice() { return _graphSlice; }

    @Override
    public String id() { return _id; }

    @Override
    public String author() { return _author; }

    @Override
    public String email() { return _email; }

    @Override
    public String comment() { return _comment; }

    @Override
    public String uri() { return _uri; }

    @Override
    public Date date() { return _date; }

    @Override
    public int compareTo(ExtendedVersionRecord o) {
        if (date() == null || o.date() == null) {
            return 0;
        }
        int v = date().compareTo(o.date());
        if (v != 0) return v;
        // If two versions for two different rules have identical time stamps (only happens when rules are edited and committed manually), we need a deterministic ordering, so fall back to rule name ordering
        return name().compareTo(o.name());
    }
}
