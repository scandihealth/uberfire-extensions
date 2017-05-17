package org.uberfire.ext.editor.commons.service.support;

import org.uberfire.backend.vfs.Path;

/**
 * Created by prc on 17-05-2017.
 */
public interface SupportsMoveToProduction {
    void moveToProduction( final Path path,
                 final String comment );
}
