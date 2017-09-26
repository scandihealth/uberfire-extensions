package org.uberfire.ext.editor.commons.client.file;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.uberfire.ext.editor.commons.client.resources.i18n.CommonConstants;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.common.popups.footers.GenericModalFooter;
import org.uberfire.mvp.Command;

import static org.uberfire.commons.validation.PortablePreconditions.*;

/**
 * Created on 30-08-2017.
 */
public class ArchivePopup extends BaseModal {

    interface ArchivePopupWidgetBinder
            extends
            UiBinder<Widget, ArchivePopup> {

    }

    private static ArchivePopupWidgetBinder uiBinder = GWT.create( ArchivePopupWidgetBinder.class );

    @UiField
    Paragraph confirmText;

    @UiField
    org.gwtbootstrap3.client.ui.Label warningLabel;

    public ArchivePopup( final Command command ) {
        checkNotNull( "command", command );
        setTitle( CommonConstants.INSTANCE.LPRArchive() );
        getElement().getStyle().setZIndex( Integer.MAX_VALUE );
        setBody( uiBinder.createAndBindUi( ArchivePopup.this ) );

        confirmText.setText( CommonConstants.INSTANCE.LPRArchivePopupConfirm() );
        warningLabel.setType( LabelType.WARNING );
        warningLabel.setText( CommonConstants.INSTANCE.LPRArchivePopupWarning() );

        final GenericModalFooter footer = new GenericModalFooter();
        footer.addButton( CommonConstants.INSTANCE.OK(),
                new Command() {
                    @Override
                    public void execute() {
                        hide();
                        command.execute();
                    }
                },
                IconType.ARCHIVE,
                ButtonType.PRIMARY );
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
