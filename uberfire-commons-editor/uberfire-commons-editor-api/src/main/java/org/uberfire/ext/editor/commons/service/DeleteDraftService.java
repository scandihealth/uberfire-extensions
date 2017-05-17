package org.uberfire.ext.editor.commons.service;

import org.jboss.errai.bus.server.annotations.Remote;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.service.support.SupportsDelete;

import java.util.Collection;

/**
 * Created by prc on 10-05-2017.
 */
@Remote
public interface DeleteDraftService extends SupportsDelete {

    /**
     * Deletes (in batch) the paths passed in {@param paths}, if they exist.
     * @param paths Paths that will be removed.
     */
    void deleteIfExists( final Collection<Path> paths,
                         final String comment );

    /**
     * Verifies if a path can be deleted.
     * @param path Path to be verified.
     * @return true if there is a restriction and the path cannot be deleted, and false otherwise.
     */
    boolean hasRestriction( Path path );
}

