/*
 * Copyright 2019 Devsoap Inc.
 *
 * Licensed under the Creative Commons Attribution-NoDerivatives 4.0
 * International Public License (the "License"); you may not use this file
 * except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *         https://creativecommons.org/licenses/by-nd/4.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.devsoap.fn.tasks

import com.devsoap.fn.util.DockerUtil
import com.devsoap.fn.util.LogUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.logging.Level

/**
 * Stops the local FN Flow servers
 *
 * @author John Ahlroos
 * @since 1.0
 */
class FnStopFlowServerTask extends DefaultTask {

    static String NAME = 'fnFlowStop'
    public static final String FLOW_SERVER = 'flowserver'
    public static final String DOCKER = 'docker'
    public static final String STOP = 'stop'
    public static final String FLOW_UI = 'flowui'

    FnStopFlowServerTask() {
        dependsOn FnStopServerTask.NAME
        description = 'Stops the local FN Flow Servers'
        group = 'fn-flow'
    }

    @TaskAction
    void run() {
        if (DockerUtil.isContainerRunning(project, FLOW_SERVER)) {
            stopFlowServer()
        }
        if (DockerUtil.isContainerRunning(project, FLOW_UI)) {
            stopFlowUIServer()
        }
    }

    private void stopFlowServer() {
        project.exec {
            commandLine DOCKER
            args STOP, FLOW_SERVER
            standardOutput = LogUtils.getLogOutputStream(Level.INFO)
            errorOutput = LogUtils.getLogOutputStream(Level.SEVERE)
        }
    }

    private void stopFlowUIServer() {
        project.exec {
            commandLine DOCKER
            args STOP, FLOW_UI
            standardOutput = LogUtils.getLogOutputStream(Level.INFO)
            errorOutput = LogUtils.getLogOutputStream(Level.SEVERE)
        }
    }

}
