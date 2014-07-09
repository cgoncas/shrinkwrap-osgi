/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.shrinkwrap.osgi.api;

import aQute.bnd.osgi.Jar;
import org.jboss.shrinkwrap.api.Assignable;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Carlos Sierra Andrés
 */
public class BndArchive implements Assignable {

    private Jar jar;

    public BndArchive(Jar jar) {
        this.jar = jar;
    }

    public Jar getJar() {
        return jar;
    }

    @Override
    public <TYPE extends Assignable> TYPE as(Class<TYPE> typeClass) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            jar.write(baos);

            return ShrinkWrap.create(ZipImporter.class).
                    importFrom(new ByteArrayInputStream(baos.toByteArray())).
                    as(typeClass);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
