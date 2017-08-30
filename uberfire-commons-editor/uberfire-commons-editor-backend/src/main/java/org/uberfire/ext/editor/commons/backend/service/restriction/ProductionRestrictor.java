package org.uberfire.ext.editor.commons.backend.service.restriction;

import javax.enterprise.context.ApplicationScoped;

import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.service.restriction.PathOperationRestriction;
import org.uberfire.ext.editor.commons.service.restrictor.DeleteRestrictor;
import org.uberfire.ext.editor.commons.service.restrictor.RenameRestrictor;

/**
 * Created on 30-08-2017.
 */
@ApplicationScoped
public class ProductionRestrictor implements DeleteRestrictor,
        RenameRestrictor {


    @Override
    public PathOperationRestriction hasRestriction( Path path ) {
        //todo ttn LPR-1317: impl that paths without meta attribute lprmeta.productionDate cannot be deleted
        //(this means only drafts can be deleted since archived rules have been in prod)
        return null;
    }
}
