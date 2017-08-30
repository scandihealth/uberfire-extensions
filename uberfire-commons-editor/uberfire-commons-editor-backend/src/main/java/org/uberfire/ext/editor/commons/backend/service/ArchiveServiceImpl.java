package org.uberfire.ext.editor.commons.backend.service;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.service.ArchiveService;

/**
 * Created by prc on 17-05-2017.
 */
@Service
@ApplicationScoped
public class ArchiveServiceImpl implements ArchiveService{
    @Override
    public void archive(Path path, String comment) {
        //todo ttn impl
    }
}
