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

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import org.uberfire.java.nio.base.version.VersionRecord;

public interface ExtendedVersionHistoryPresenterView
        extends IsWidget {



    interface Presenter {

        void onSelect(ExtendedVersionRecord record);

    }

    void refreshGrid();

    void setup(String version, AsyncDataProvider<ExtendedVersionRecord> dataProvider);

    void setPresenter(Presenter presenter);

    void showLoading();
}