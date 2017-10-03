/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.ext.metadata.search;

import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Sort;
import org.uberfire.ext.metadata.model.KObject;
import org.uberfire.java.nio.file.Path;

public interface IOSearchService {

    List<Path> searchByAttrs( final Map<String, ?> attrs,
                              final Filter filter,
                              final Path... roots );

    List<Path> searchByAttrs( final Map<String, ?> attrs,
                              final Filter filter,
                              final Sort sortOrder,
                              final Path... roots );

    List<Path> fullTextSearch( final String term,
                               final Filter filter,
                               final Path... roots );

    int searchByAttrsHits( final Map<String, ?> attrs,
                           final Path... roots );

    int fullTextSearchHits( final String term,
                            final Path... roots );

    interface Filter {

        boolean accept( final KObject kObject );

    }

    class NoOpFilter implements Filter {

        @Override
        public boolean accept( final KObject kObject ) {
            return true;
        }
    }

}
