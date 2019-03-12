/*
*  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.developerstudio.eclipse.esb.project.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.wso2.developerstudio.eclipse.esb.project.connector.store.Connector;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.ui.Activator;

import com.google.gson.Gson;

/**
 * Servlet that contains connector related actions
 */
public class ConnectorServlet extends HttpServlet {

    private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

    /**
     * Retrieves all connectors
     * 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        ConnectorServletUtil connectorFunctionServletUtil = new ConnectorServletUtil();
        response.getWriter().println(connectorFunctionServletUtil.getConnectorsList());
    }

    /**
     * Downloads the specified connector
     * 
     * Response contains the container to be downloaded as {"data":{"id": "xx", "attributes": []}}
     * 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String function = request.getParameter("data");
        Connector connector = new Gson().fromJson(function, Connector.class);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");
        new Thread(new Runnable() {
            public void run() {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        ConnectorServletUtil connectorFunctionServletUtil = new ConnectorServletUtil();
                        Job downloadJob = new Job("Downloading Connectors") {
                            @Override
                            protected IStatus run(IProgressMonitor monitor) {
                                monitor.beginTask("Downloading connector", 100);
                                monitor.subTask(connector.getAttributes().getOverview_name() + " connector");
                                String downloadLink = connector.getAttributes().getOverview_downloadlink();
                                if (connectorFunctionServletUtil.downloadConnectorAndUpdateProjects(downloadLink)) {
                                    monitor.worked(100);
                                    monitor.done();
                                    return Status.OK_STATUS;
                                } else {

                                }
                                monitor.worked(100);
                                monitor.done();
                                return Status.CANCEL_STATUS;
                            }
                        };
                        downloadJob.schedule();
                    }
                });
            }
        }).start();
    }

}