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

    /**
     * Removes the version records pertaining to modification of the uberfire metadata "dot" file from the input collection
     * @param records the list to remove elements from
     */
    //NOTE: This method cannot be unit tested because the regular expression is javascript-specific
    static void removeMetadataFileRevisions( List<VersionRecord> records ) {
        Iterator<VersionRecord> iter = records.iterator();
        while ( iter.hasNext() ) {
            VersionRecord record = iter.next();
            if ( record != null && record.comment() != null && record.comment().contains( "delete {" ) ) {
                // potentiel match - make the more fine grained test:
                // there must be exactly two . chars (with other non-whitespace chars in between) after { and before }
                RegExp regExp = RegExp.compile( "{\\/\\S[^.]+\\.\\S[^.]+\\.\\S[^.]+}" );
                MatchResult matcher = regExp.exec( record.comment() );
                if ( matcher != null ) { //match found
                    iter.remove(); //remove irrelevant version record
                }
            }
        }
    }
}
