/*
 * Copyright 2015 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.editor.commons.client.history;

import java.util.Date;

import com.google.gwt.cell.client.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.AsyncDataProvider;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.uberfire.ext.editor.commons.client.resources.i18n.CommonConstants;
import org.uberfire.ext.widgets.common.client.common.BusyPopup;
import org.uberfire.ext.widgets.common.client.tables.SimpleTable;
import org.uberfire.java.nio.base.version.VersionRecord;

import javax.persistence.criteria.CriteriaBuilder;

public class ExtendedVersionHistoryPresenterViewImpl
        extends Composite
        implements ExtendedVersionHistoryPresenterView {

    private SimpleTable table = new SimpleTable();

    private Presenter presenter;
    private String version;

    public ExtendedVersionHistoryPresenterViewImpl() {
        initWidget( table );

        table.getElement().setAttribute( "data-uf-lock", "false" );

        Column<ExtendedVersionRecord, String> columnButton = new Column<ExtendedVersionRecord, String>( new ButtonCell() ) {
            @Override
            public String getValue( ExtendedVersionRecord object ) {
                if ( version.equals( object.id() ) ) {
                    return CommonConstants.INSTANCE.Current();
                } else {
                    return CommonConstants.INSTANCE.Select();
                }
            }
        };
        table.addColumn( columnButton, "" );
        columnButton.setFieldUpdater( new FieldUpdater<ExtendedVersionRecord, String>() {
            @Override
            public void update( int index,
                                ExtendedVersionRecord record,
                                String value ) {
                presenter.onSelect( record ); //XXX need to be able to select not only different version but different rule / uri
            }
        } );

        Column<ExtendedVersionRecord, String> columnGraph = new Column<ExtendedVersionRecord, String>( new TextCell() {
            @Override
            public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
                // We sneaked the html graph throught the SafeHtml conversion in disguise,
                // now decode it before putting it back in as raw HTML to be rendered.
                // (We could have used a Base64 library, but with GWT these external libraries are always a hassle).
                String x = "";
                String[] chars = value.asString().split(",");
                for (String s : chars) {
                    if (s.length() > 0) {
                        int i = Integer.decode("0x" + s);
                        x += Character.toString((char)i);
                    }
                }
                sb.appendHtmlConstant(x);
            }
        } ) {
            @Override
            public String getCellStyleNames(Cell.Context context, ExtendedVersionRecord object) {
                return "novborder";
            }
            @Override
            public String getValue( ExtendedVersionRecord object ) {
                // Since GWT will take our returned graphSlice string value and convert it to SafeHtml
                // before passing it to render above, and since our graphSlice string
                // on purpose contains machine-generated, safe HTML to be rendered as such,
                // we'll encode the string to something that SafeHtml will not mess up:
                String x = "";
                String original = object.graphSlice();
                for (int i = 0; i < original.length(); i++) {
                    char originalChar = original.charAt(i);
                    x += "," + Integer.toHexString(originalChar);
                }
                return x;
            }
        };
        table.addColumn( columnGraph, "Historik" );

        Column<ExtendedVersionRecord, String> columnName =
                new Column<ExtendedVersionRecord, String>( new TextCell() ) {
                    @Override
                    public String getValue( ExtendedVersionRecord object ) {
                        return object.name();
                    }
                };
        table.addColumn( columnName, "Regel" );

        Column<ExtendedVersionRecord, String> columnVersion = new Column<ExtendedVersionRecord, String>( new TextCell() ) {
            @Override
            public String getValue( ExtendedVersionRecord object ) {
                return object.versionNumber();
            }
        };
        table.addColumn( columnVersion, "Version" );

        Column<ExtendedVersionRecord, Date> columnDate = new Column<ExtendedVersionRecord, Date>( new DateCell() ) {
            @Override
            public Date getValue( ExtendedVersionRecord object ) {
                return object.date();
            }
        };
        table.addColumn( columnDate, CommonConstants.INSTANCE.Date() );

        Column<ExtendedVersionRecord, String> columnComment = new Column<ExtendedVersionRecord, String>( new TextCell() ) {
            @Override
            public String getValue( ExtendedVersionRecord object ) {
                return object.comment();
            }
        };
        table.addColumn( columnComment, CommonConstants.INSTANCE.CommitMessage() );

        Column<ExtendedVersionRecord, String> columnAuthor = new Column<ExtendedVersionRecord, String>( new TextCell() ) {

            @Override
            public String getValue( ExtendedVersionRecord object ) {
                return object.author();
            }
        };
        table.addColumn( columnAuthor, CommonConstants.INSTANCE.Author() );


        table.setWidth("100%");
        table.setColumnWidth(columnButton, 80.0, Style.Unit.PX);
        table.setColumnWidth(columnGraph, 90.0, Style.Unit.PX);
        table.setColumnWidth(columnName, 150.0, Style.Unit.PX);
        table.setColumnWidth(columnVersion, 50.0, Style.Unit.PX);
        table.setColumnWidth(columnDate, 30.0, Style.Unit.PCT);
        table.setColumnWidth(columnComment, 60.0, Style.Unit.PCT);
        table.setColumnWidth(columnAuthor, 10.0, Style.Unit.PCT);
    }

    @Override
    public void setup( String version,
                       AsyncDataProvider<ExtendedVersionRecord> dataProvider ) {
        this.version = version;
        if ( !dataProvider.getDataDisplays().contains( table ) ) {
            dataProvider.addDataDisplay( table );
        }
    }

    @Override
    public void refreshGrid() {
        table.refresh();
    }

    @Override
    public void setPresenter( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        BusyPopup.showMessage( CommonConstants.INSTANCE.Loading() );
    }

    @Override
    public void hideLoading() {
        BusyPopup.close();
    }
}
