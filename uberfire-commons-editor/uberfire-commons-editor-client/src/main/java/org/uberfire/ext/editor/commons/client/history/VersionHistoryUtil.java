package org.uberfire.ext.editor.commons.client.history;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import org.uberfire.java.nio.base.version.VersionRecord;

/**
 * Created on 05-10-2017.
 */
class VersionHistoryUtil {
    // A dot file revision has exactly two . chars (with other non-whitespace chars in between) after { and before }
    private static RegExp dotFileRevision = RegExp.compile( "{\\/\\S[^.]+\\/\\.\\S+\\.\\S[^.]+}" );

    /**
     * Removes the version records pertaining to modification of the uberfire metadata "dot" file from the input collection
     * @param records the list to remove elements from
     */
    //NOTE: This method cannot be unit tested because the regular expression is javascript-specific
    static void removeDotFileRevisions( List<VersionRecord> records ) {
        Iterator<VersionRecord> iter = records.iterator();
        while ( iter.hasNext() ) {
            VersionRecord record = iter.next();
            MatchResult matcher = dotFileRevision.exec( record.comment() );
            if ( matcher != null ) { //match found
                iter.remove(); //remove irrelevant version record
            }
        }
    }
}
