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
package com.devsoap.fn

import com.devsoap.fn.actions.FnPluginAction
import com.devsoap.fn.actions.PluginAction
import com.devsoap.fn.extensions.FnExtension
import com.devsoap.fn.tasks.FnCreateFunctionTask
import com.devsoap.fn.tasks.FnDeployTask
import com.devsoap.fn.tasks.FnInstallCliTask
import com.devsoap.fn.tasks.FnInvokeTask
import com.devsoap.fn.tasks.FnPrepareDockerTask
import com.devsoap.fn.tasks.FnStartFlowServerTask
import com.devsoap.fn.tasks.FnStartServerTask
import com.devsoap.fn.tasks.FnStopFlowServerTask
import com.devsoap.fn.tasks.FnStopServerTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject

/**
 * Builder for creating writers for writing a template to the file system
 *
 * @author John Ahlroos
 * @since 1.0
 */
class FnPlugin implements Plugin<Project> {

    static final String PLUGIN_ID = 'com.devsoap.fn'
    static final String PRODUCT_NAME = 'gradle-fn-plugin'

    private final List<PluginAction> actions = []

    @Inject
    FnPlugin(Instantiator instantiator) {
        actions << instantiator.newInstance(FnPluginAction)
    }

    @Override
    void apply(Project project) {

        project.rootProject.with {
            tasks.with {
                if (!findByName(FnStartServerTask.NAME)) {
                    register(FnStartServerTask.NAME, FnStartServerTask)
                }
                if (!findByName(FnStopServerTask.NAME)) {
                    register(FnStopServerTask.NAME, FnStopServerTask)
                }
                if (!findByName(FnStartFlowServerTask.NAME)) {
                    register(FnStartFlowServerTask.NAME, FnStartFlowServerTask)
                }
                if (!findByName(FnStopFlowServerTask.NAME)) {
                    register(FnStopFlowServerTask.NAME, FnStopFlowServerTask)
                }
                if (!findByName(FnInstallCliTask.NAME)) {
                    register(FnInstallCliTask.NAME, FnInstallCliTask)
                }
                if (!findByName(FnCreateFunctionTask.NAME)) {
                    register(FnCreateFunctionTask.NAME, FnCreateFunctionTask)
                }
            }
        }

        project.with {
            actions.each { action ->
                action.apply(project)
            }
            tasks.with {
                register(FnPrepareDockerTask.NAME, FnPrepareDockerTask)
                register(FnDeployTask.NAME, FnDeployTask)
                register(FnInvokeTask.NAME, FnInvokeTask)
                if (!findByName(FnCreateFunctionTask.NAME)) {
                    register(FnCreateFunctionTask.NAME, FnCreateFunctionTask)
                }
            }
            extensions.with {
                create(FnExtension.NAME, FnExtension, project)
            }
        }
    }
}
