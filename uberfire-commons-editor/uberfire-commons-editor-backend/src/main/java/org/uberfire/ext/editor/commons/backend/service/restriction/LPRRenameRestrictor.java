package org.uberfire.ext.editor.commons.backend.service.restriction;

import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.service.restriction.PathOperationRestriction;
import org.uberfire.ext.editor.commons.service.restrictor.RenameRestrictor;

/**
 * Created on 04-09-2017.
 */
public class LPRRenameRestrictor implements RenameRestrictor {
    @Override
    public PathOperationRestriction hasRestriction( Path path ) {
        //todo ttn LPR-1317: impl that paths with meta attribute lprmeta.productionDate cannot be renamed
        return null;
    }
}
