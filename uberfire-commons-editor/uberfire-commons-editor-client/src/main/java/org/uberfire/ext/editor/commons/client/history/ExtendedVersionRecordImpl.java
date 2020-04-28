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

    public ExtendedVersionRecordImpl(
            String name,
            String versionNumber,
            String graphSlice,
            String id,
            String author,
            String email,
            String comment,
            String uri,
            Date date) {
        _name = name;
        _versionNumber = versionNumber;
        _graphSlice = graphSlice;
        _id = id;
        _author = author;
        _email = email;
        _comment = comment;
        _uri = uri;
        _date = date;
    }

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
        return date().compareTo(o.date());
    }
}
