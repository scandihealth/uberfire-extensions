package org.uberfire.ext.editor.commons.service.support;

import org.uberfire.backend.vfs.Path;

/**
 * Created by prc on 17-05-2017.
 */
public interface SupportsArchive {
    void archive( final Path path,
                           final String comment );
}
