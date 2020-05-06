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

import java.util.*;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.ext.editor.commons.client.history.event.VersionSelectedEvent;
import org.uberfire.ext.editor.commons.version.VersionService;
import org.uberfire.java.nio.base.version.VersionRecord;
import org.uberfire.mvp.ParameterizedCommand;

public class ExtendedVersionHistoryPresenter
        implements ExtendedVersionHistoryPresenterView.Presenter,
        IsWidget {

    private ExtendedVersionHistoryPresenterView view;
    private Caller<VersionService> versionService;

    private AsyncDataProvider<ExtendedVersionRecord> dataProvider;

    private Event<VersionSelectedEvent> versionSelectedEvent;

    private Path path;
    private String fetchName;
    private ExtendedVersionGraph versionGraph;
    private String version;
    private List<ExtendedVersionRecord> records;
    private ParameterizedCommand<VersionRecord> onCurrentVersionRefreshed;

    @Inject
    public ExtendedVersionHistoryPresenter(
            final ExtendedVersionHistoryPresenterView view,
            Caller<VersionService> versionService,
            Event<VersionSelectedEvent> versionSelectedEvent) {
        this.view = view;
        this.versionService = versionService;
        this.versionSelectedEvent = versionSelectedEvent;

        view.setPresenter(this);
        dataProvider = new AsyncDataProvider<ExtendedVersionRecord>() {
            @Override
            protected void onRangeChanged(HasData<ExtendedVersionRecord> display) {
                if (records != null) {
                    updateRowCount(records.size(), true);
                    updateRowData(0, records);
                }
            }
        };

    }

    public void init(final Path path) {
        this.path = path;
    }

    private void loadContent() {
        records = new ArrayList<ExtendedVersionRecord>();
        fetchName = path.getFileName();
        versionGraph = new ExtendedVersionGraph();
        versionService.call(getRemoteCallback()).getVersions(path);
    }

    private RemoteCallback<List<VersionRecord>> getRemoteCallback() {
        return new RemoteCallback<List<VersionRecord>>() {
            @Override
            public void callback(List<VersionRecord> fetchedRecords) {
                VersionHistoryUtil.removeDotFileRevisions( fetchedRecords );

                boolean isFirstBatch = (records.size() == 0);
                // fetchedRecords from the version service are always sorted with first version first. Now convert each to an extended record
                int versionCounter = 0;
                for (VersionRecord r: fetchedRecords) {
                    versionCounter++;
                    records.add(new ExtendedVersionRecordImpl(
                            fetchName.substring(0, fetchName.lastIndexOf('.')),
                            "" + versionCounter,
                            null,
                            r.id(),
                            r.author(),
                            r.email(),
                            r.comment(),
                            r.uri(),
                            r.date(),
                            isFirstBatch
                    ));
                }

                if (fetchedRecords.size() > 0) {
                    // First, record a 'branch' for the current rule
                    VersionRecord first = fetchedRecords.get(0);
                    VersionRecord last = fetchedRecords.get(fetchedRecords.size() -1);
                    versionGraph.addBranch(fetchName.substring(0, fetchName.lastIndexOf('.')), first.date(), last.date());

                    // If first version of this rule was copied or renamed from another rule, recursively fetch
                    // the records for the ancestor. This continues until we have reached the first known ancestor.
                    // Note that with the async version service interface, we just fire off a new request for the ancestor rule
                    // with this same callback, and return. The callback will be called again with the new version set
                    // and continue the work until we have reached ancestor zero.
                    String desc =first.comment();
                    if (desc.indexOf(" copied to ") >= 0 || desc.indexOf(" renamed to ") >= 0) {
                        RegExp p = RegExp.compile("\\[([^\\]]*)\\][^\\[]*\\[([^\\]]*)\\]");
                        MatchResult m = p.exec(desc);
                        if (m != null) {
                            if (m.getGroupCount() >= 2) {
                                // Successfully matched pattern; Now extract URI for ancestor rule.
                                String uri = m.getGroup(1);
                                String[] fragments = uri.split("/");
                                fetchName = fragments[fragments.length - 1];
                                // And call version service again... We'll be back.
                                versionService.call(getRemoteCallback()).getVersions(PathFactory.newPath(fetchName, uri));
                                return;
                            }
                        }
                    }
                }

                // Finally collected and aggregated all versions for the entire tree (and registered branches along the way).
                // Now sort everything chronologically before annotating the records with the slices of the version graph.
                Collections.sort(records);
                versionGraph.decorate(records);

                view.setup(version, dataProvider);
                if (records != null) {
                    dataProvider.updateRowCount(records.size(), true);
                    dataProvider.updateRowData(0, records);
                }
                view.refreshGrid();
                doOnCurrentVersionRefreshed( version );
            }
        };
    }

    @Override
    public void onSelect(ExtendedVersionRecord record) {
        if (!record.isCurrentRule()) {
            // TODO When selecting a version of a different rule/branch, we can't just switch version here,
            // we need a full reload of the other rule into the editor - presumably something that hits the encapsulating BaseEditor
            // similarly to restoreEvent.fire( new RestoreEvent( restoreUtil.createObservablePath( restored, currentVersionRecordUri ) ) );
            return;
        }
        if (!record.id().equals(version)) {
            view.showLoading();
            versionSelectedEvent.fire(
                    new VersionSelectedEvent(
                            path,
                            record
                    ));
        }
    }

    public void onVersionChange(@Observes VersionSelectedEvent event) {
        if (path != null) {
            if (path.toURI().equals(event.getPathToFile().toURI())) {
                version = event.getVersionRecord().id();
                loadContent();
            }
        }
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    public void refresh(String version) {
        this.version = version;
        loadContent();
    }

    public void refresh() {
        view.refreshGrid();
    }

    public void setOnCurrentVersionRefreshed( ParameterizedCommand<VersionRecord> onCurrentVersionRefreshed ) {
        this.onCurrentVersionRefreshed = onCurrentVersionRefreshed;
    }

    private void doOnCurrentVersionRefreshed( String version ) {
        if ( onCurrentVersionRefreshed != null && records != null && version != null ) {
            for ( VersionRecord record : records ) {
                if ( version.equals( record.id() ) ) {
                    onCurrentVersionRefreshed.execute( record );
                    break;
                }
            }
        }
    }
}
