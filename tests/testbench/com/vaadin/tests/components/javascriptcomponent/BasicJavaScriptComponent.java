/* 
 * Copyright 2011 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.components.javascriptcomponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.external.json.JSONArray;
import com.vaadin.external.json.JSONException;
import com.vaadin.shared.communication.ClientRpc;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.JavaScriptComponentState;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.terminal.gwt.server.ResourceReference;
import com.vaadin.tests.components.AbstractTestUI;
import com.vaadin.tests.util.Log;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.JavaScriptFunction;

public class BasicJavaScriptComponent extends AbstractTestUI {

    public interface TestRpc extends ServerRpc, ClientRpc {
        public void sendRpc(String message);
    }

    public static class TestState extends JavaScriptComponentState {
        private List<String> messages = new ArrayList<String>();
        private URLReference url;

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }

        public URLReference getUrl() {
            return url;
        }

        public void setUrl(URLReference url) {
            this.url = url;
        }
    }

    @JavaScript("BasicJavaScriptComponentConnector.js")
    public class ExampleWidget extends AbstractJavaScriptComponent {
        public ExampleWidget() {
            registerRpc(new TestRpc() {
                @Override
                public void sendRpc(String message) {
                    log.log("Got RPC message: " + message);
                }
            });
            addFunction("messageToServer", new JavaScriptFunction() {
                @Override
                public void call(JSONArray arguments) throws JSONException {
                    log.log("Got callback message: " + arguments.getString(0));
                }
            });

            addFunction("reportParentIds", new JavaScriptFunction() {
                @Override
                public void call(JSONArray arguments) throws JSONException {
                    JSONArray parentIds = arguments.getJSONArray(0);
                    if (!parentIds.getString(0).equals(getConnectorId())) {
                        log.log("Connector ids doesn't match");
                    }

                    HasComponents parent = getParent();
                    int i = 1;
                    while (parent != null) {
                        if (!parentIds.getString(i).equals(
                                parent.getConnectorId())) {
                            log.log("parentIds[" + i + "] doesn't match");
                        }
                        i++;
                        parent = parent.getParent();
                    }
                    log.log("Parent ids checked");
                }
            });

            getRpcProxy(TestRpc.class).sendRpc("RPC message");
            callFunction("messageToClient", "Callback message");

            getState()
                    .setMessages(
                            Arrays.asList("First state message",
                                    "Second state message"));
            ClassResource resource = new ClassResource("test",
                    BasicJavaScriptComponent.this.getApplication());
            getState().setUrl(new ResourceReference(resource));
        }

        @Override
        public TestState getState() {
            return (TestState) super.getState();
        }
    }

    private final Log log = new Log(5);

    @Override
    protected void setup(WrappedRequest request) {
        addComponent(log);

        ExampleWidget c = new ExampleWidget();
        c.setCaption("Component caption");
        addComponent(c);
    }

    @Override
    protected String getTestDescription() {
        return "Test for basic JavaScript component functionality.";
    }

    @Override
    protected Integer getTicketNumber() {
        return Integer.valueOf(8888);
    }

}
