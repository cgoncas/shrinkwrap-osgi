/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.jboss.shrinkwrap.osgi.api;

import org.jboss.shrinkwrap.api.Assignable;

import java.io.File;

/**
 * @author Carlos Sierra Andrés
 */
public interface BndProjectBuilder extends Assignable {

	BndProjectBuilder setWorkspace(File file);

	BndProjectBuilder setProject(File file);

	BndProjectBuilder setBase(File file);

	BndProjectBuilder setBndFile(File file);
}
