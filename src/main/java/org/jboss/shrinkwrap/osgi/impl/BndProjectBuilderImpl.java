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

package org.jboss.shrinkwrap.osgi.impl;

import aQute.bnd.build.Project;
import aQute.bnd.build.ProjectBuilder;
import aQute.bnd.build.Workspace;
import aQute.bnd.osgi.Processor;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Assignable;
import org.jboss.shrinkwrap.osgi.api.BndArchive;
import org.jboss.shrinkwrap.osgi.api.BndProjectBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Carlos Sierra Andrés
 */
public class BndProjectBuilderImpl implements BndProjectBuilder {

	private boolean generateManifest = true;

	private File workspaceFile = null;
	private File projectFile = null;
	private File baseFile = null;
	private File bndFile = null;
	private List<File> classPath = new ArrayList<>();
    private List<File> projectPropertiesFiles = new ArrayList<>();
    private List<File> workspacePropertiesFiles = new ArrayList<>();

	public BndProjectBuilderImpl(Archive<?> archive) {

	}

	@Override
	public BndProjectBuilder addClassPath(File file) {
		this.classPath.add(file);

		return this;
	}

	@Override
	public <TYPE extends Assignable> TYPE as(Class<TYPE> typeClass) {
        try {
            return asBndJar().as(typeClass);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public BndArchive asBndJar() {
        try {
            Workspace workspace = new Workspace(this.workspaceFile);

            Properties workspaceProperties = buildProperties(workspace, null, workspacePropertiesFiles.toArray(new File[0]));

            workspace.setProperties(workspaceProperties);

            Project project = new Project(workspace, this.projectFile);

            Properties projectProperties = buildProperties(project, bndFile, projectPropertiesFiles.toArray(new File[0]));

            project.setProperties(projectProperties);

            ProjectBuilder projectBuilder = new ProjectBuilder(project);

            projectBuilder.setBase(baseFile);

			for (File file : classPath) {
				projectBuilder.addClasspath(file);
			}

            if (!generateManifest) {
                projectBuilder.setProperty(ProjectBuilder.NOMANIFEST, "true");
            }

            return new BndArchive(projectBuilder.build());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected Properties buildProperties(Processor processor, File propertiesFile, File ... extraFiles) throws IOException {
        Properties properties = new Properties();

        for (File extraFile : extraFiles) {
            properties.putAll(processor.loadProperties(extraFile));
        }

        if (propertiesFile != null) {
            properties.putAll(processor.loadProperties(propertiesFile));
        }

        return properties;
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
    public BndProjectBuilder addProjectPropertiesFile(File file) {
        projectPropertiesFiles.add(file);

        return this;
    }

    @Override
    public BndProjectBuilder addWorkspacePropertiesFile(File file) {
        workspacePropertiesFiles.add(file);

        return this;
    }

    @Override
	public BndProjectBuilder setWorkspace(File workspace) {
		this.workspaceFile = workspace;

		return this;
	}
}
