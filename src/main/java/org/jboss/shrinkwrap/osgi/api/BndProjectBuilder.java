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

import org.jboss.shrinkwrap.api.Assignable;

import java.io.File;

/**
 * @author Carlos Sierra Andrés
 */
public interface BndProjectBuilder extends Assignable {

	BndProjectBuilder addClassPath(File file);

	BndProjectBuilder addProjectPropertiesFile(File file);

	BndProjectBuilder addWorkspacePropertiesFile(File file);

	BndProjectBuilder setWorkspace(File file);

	BndProjectBuilder setProject(File file);

	BndProjectBuilder setBase(File file);

	BndProjectBuilder setBndFile(File file);

	BndProjectBuilder generateManifest(boolean enableAnalyze);

	BndArchive asBndJar();
}
