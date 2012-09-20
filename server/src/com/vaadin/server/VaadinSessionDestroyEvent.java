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

package com.vaadin.server;

import java.util.EventObject;

/**
 * Event fired when a Vaadin session is no longer in use.
 * 
 * @see VaadinSessionDestroyListener#vaadinSessionDestroyed(VaadinSessionDestroyEvent)
 * 
 * @author Vaadin Ltd
 * @since 7.0.0
 */
public class VaadinSessionDestroyEvent extends EventObject {

    private final VaadinSession session;

    /**
     * Creates a new event.
     * 
     * @param service
     *            the Vaadin service from which the even originates
     * @param session
     *            the Vaadin session that is no longer used
     */
    public VaadinSessionDestroyEvent(VaadinService service,
            VaadinSession session) {
        super(service);
        this.session = session;
    }

    @Override
    public VaadinService getSource() {
        return (VaadinService) super.getSource();
    }

    /**
     * Gets the Vaadin service from which the even originates.
     * 
     * @return the Vaadin service
     */
    public VaadinService getVaadinService() {
        return getSource();
    }

    /**
     * Gets the Vaadin session that is no longer used.
     * 
     * @return the Vaadin session
     */
    public VaadinSession getVaadinSession() {
        return session;
    }

}