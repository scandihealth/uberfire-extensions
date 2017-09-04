package org.uberfire.ext.editor.commons.backend.service.restriction;

import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.service.restriction.PathOperationRestriction;
import org.uberfire.ext.editor.commons.service.restrictor.DeleteRestrictor;

/**
 * Created on 31-08-2017.
 */
public class LPRDeleteRestrictor implements DeleteRestrictor {
    @Override
    public PathOperationRestriction hasRestriction( Path path ) {
        //todo ttn LPR-1317: impl that paths with meta attribute lprmeta.productionDate cannot be deleted
        //(this means only drafts can be deleted since archived rules have been in prod)

        return null;
    }
}
