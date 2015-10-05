package com.cumulativeminds.zeus.api.controller;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public abstract class ModelAwareController {
    protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    
    protected ModelAwareController() {
    }

}
