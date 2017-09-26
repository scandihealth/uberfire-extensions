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
public class MoveToProductionPopup extends BaseModal {

    interface MoveToProductionPopupWidgetBinder
            extends
            UiBinder<Widget, MoveToProductionPopup> {

    }

    private static MoveToProductionPopupWidgetBinder uiBinder = GWT.create( MoveToProductionPopupWidgetBinder.class );

    @UiField
    Paragraph confirmText;

    @UiField
    org.gwtbootstrap3.client.ui.Label warningLabel;

    public MoveToProductionPopup( final Command command ) {
        checkNotNull( "command", command );
        setTitle( CommonConstants.INSTANCE.LPRMoveToProduction() );
        getElement().getStyle().setZIndex( Integer.MAX_VALUE );
        setBody( uiBinder.createAndBindUi( MoveToProductionPopup.this ) );

        confirmText.setText( CommonConstants.INSTANCE.LPRMoveToProductionPopupConfirm() );
        warningLabel.setType( LabelType.WARNING );
        warningLabel.setText( CommonConstants.INSTANCE.LPRMoveToProductionPopupWarning() );

        final GenericModalFooter footer = new GenericModalFooter();
        footer.addButton( CommonConstants.INSTANCE.OK(),
                new Command() {
                    @Override
                    public void execute() {
                        hide();
                        command.execute();
                    }
                },
                IconType.GEARS,
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
