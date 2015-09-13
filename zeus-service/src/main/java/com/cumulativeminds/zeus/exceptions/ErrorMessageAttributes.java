package com.cumulativeminds.zeus.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public enum ErrorMessageAttributes {
    /**
     * Holds redundantly the HTTP error status code, so that the developer can
     * "see" it without having to analyze the response’s header
     */
    status, /**
             * This is an internal code specific to the API (should be more
             * relevant for business exceptions)
             **/
    code, /**
           * Short description of the error, what might have cause it and
           * possibly a "fixing" proposal
           */
    message, /**
              * Points to <a href="http://itwiki">IT Wiki</a> resource, where
              * more details can be found about the error
              */
    link, /**
           * Detailed message, containing additional data that might be relevant
           * to the developer. This should only be available when the "debug"
           * mode is switched on and could potentially contain stack trace
           * information or something similar.
           */
    developerMessage,

    timestamp;
}
