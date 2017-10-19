package org.uberfire.ext.editor.commons.client.file;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.uberfire.ext.editor.commons.client.resources.i18n.CommonConstants;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.common.popups.footers.GenericModalFooter;
import org.uberfire.mvp.Command;

import static org.uberfire.commons.validation.PortablePreconditions.*;

/**
 * Created on 18-10-2017.
 */
public class LPRDeletePopup extends BaseModal {

    interface LPRDeletePopupWidgetBinder
            extends
            UiBinder<Widget, LPRDeletePopup> {
    }

    private static LPRDeletePopupWidgetBinder uiBinder = GWT.create( LPRDeletePopupWidgetBinder.class );

    @UiField
    Paragraph confirmText;

    @UiField
    Paragraph warningText;

    public LPRDeletePopup( final Command command, boolean isRestoreCommand ) {
        checkNotNull( "command", command );
        setTitle( CommonConstants.INSTANCE.LPRDeletePopupTitle() );
        getElement().getStyle().setZIndex( Integer.MAX_VALUE );
        setBody( uiBinder.createAndBindUi( LPRDeletePopup.this ) );
        warningText.setText( isRestoreCommand ? CommonConstants.INSTANCE.LPRDeletePopupRestoreWarning() : CommonConstants.INSTANCE.LPRDeletePopupDeleteWarning() );
        confirmText.setText( CommonConstants.INSTANCE.LPRDeletePopupConfirm() );

        final GenericModalFooter footer = new GenericModalFooter();
        footer.addButton( isRestoreCommand ? CommonConstants.INSTANCE.LPRDeletePopupRestore() : CommonConstants.INSTANCE.LPRDeletePopupDelete(),
                new Command() {
                    @Override
                    public void execute() {
                        hide();
                        command.execute();
                    }
                },
                isRestoreCommand ? IconType.UNDO : IconType.MINUS,
                isRestoreCommand ? ButtonType.PRIMARY : ButtonType.DANGER );
        footer.addButton( CommonConstants.INSTANCE.Cancel(),
                new Command() {
                    @Override
                    public void execute() {
                        hide();
                    }
                },
                ButtonType.DEFAULT );
        add( footer );
    }

}
