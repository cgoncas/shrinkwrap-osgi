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

package org.jboss.shrinkwrap.osgi.impl;

import aQute.bnd.build.Project;
import aQute.bnd.build.ProjectBuilder;
import aQute.bnd.build.Workspace;
import aQute.bnd.osgi.Jar;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Assignable;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.osgi.api.BndProjectBuilder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author Carlos Sierra Andrés
 */
public class BndProjectBuilderImpl implements BndProjectBuilder {

	private boolean generateManifest = true;

	private File workspaceFile = null;
	private File projectFile = null;
	private File baseFile = null;
	private File bndFile = null;

	public BndProjectBuilderImpl(Archive<?> archive) {

	}

	@Override
	public <TYPE extends Assignable> TYPE as(Class<TYPE> typeClass) {
		if (!(JavaArchive.class.isAssignableFrom(typeClass))) {
			throw new NotImplementedException();
		}
		try {
			Workspace workspace = new Workspace(this.workspaceFile);

			Project project = new Project(workspace, this.projectFile, bndFile);

			ProjectBuilder projectBuilder = new ProjectBuilder(project);

			projectBuilder.setBase(baseFile);

			if (!generateManifest) {
				projectBuilder.setProperty(ProjectBuilder.NOMANIFEST, "true");
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Jar jar = projectBuilder.build();

			jar.write(baos);

			return ShrinkWrap.create(ZipImporter.class).
					importFrom(new ByteArrayInputStream(baos.toByteArray())).
					as(typeClass);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public BndProjectBuilder setBase(File base) {
		if (workspaceFile == null)
			setWorkspace(base);
		if (projectFile == null)
			setProject(base);
		this.baseFile = base;

		return this;
	}

	@Override
	public BndProjectBuilder setBndFile(File bnd) {
		File bndParentDir = bnd.getAbsoluteFile().getParentFile();

		if (workspaceFile == null)
			setWorkspace(bndParentDir);
		if (projectFile == null)
			setProject(bndParentDir);
		if (baseFile == null)
			setBase(bndParentDir);

		this.bndFile = bnd;

		return this;
	}

	@Override
	public BndProjectBuilder generateManifest(boolean enableAnalyze) {
		this.generateManifest = enableAnalyze;

		return this;
	}

	@Override
	public BndProjectBuilder setProject(File project) {
		if (workspaceFile == null)
			setWorkspace(project);

		this.projectFile = project;

		return this;
	}

	@Override
	public BndProjectBuilder setWorkspace(File workspace) {
		this.workspaceFile = workspace;

		return this;
	}
}
