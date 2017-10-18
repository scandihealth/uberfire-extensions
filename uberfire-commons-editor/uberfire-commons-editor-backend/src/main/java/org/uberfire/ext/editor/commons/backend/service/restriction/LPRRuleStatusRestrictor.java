package org.uberfire.ext.editor.commons.backend.service.restriction;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

import org.guvnor.common.services.shared.metadata.model.LprMetadataConsts;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.service.restriction.PathOperationRestriction;
import org.uberfire.io.IOService;

/**
 * Created on 04-09-2017.
 */
//@ApplicationScoped
//todo ttn consider what to do with this restrictor - reimplement where it checks all rule versions to see if any is in prod?
public class LPRRuleStatusRestrictor /*implements DeleteRestrictor, RenameRestrictor*/ {

    @Inject
    @Named("ioStrategy")
    private IOService ioService;

//    @Override
    public PathOperationRestriction hasRestriction( Path path ) {
        org.uberfire.java.nio.file.Path filePath = ioService.get( path.toURI() );
        Map<String, Object> fileAttributes = ioService.readAttributes( filePath );
        Long productionDate = ( Long ) fileAttributes.get( LprMetadataConsts.PRODUCTION_DATE );
        Long archivedDate = ( Long ) fileAttributes.get( LprMetadataConsts.ARCHIVED_DATE );

        if ( archivedDate != null && archivedDate > 0 ) {
            return new PathOperationRestriction() {
                @Override
                public String getMessage( final Path path ) {
                    return path.getFileName() + " cannot be deleted, moved or renamed. It is archived";
                }
            };
        }
        if ( productionDate != null && productionDate > 0 ) {
            return new PathOperationRestriction() {
                @Override
                public String getMessage( final Path path ) {
                    return path.getFileName() + " cannot be deleted, moved or renamed. It is in production";
                }
            };
        }
        return null;
    }
}
