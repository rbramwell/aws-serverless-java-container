/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.serverless.proxy.internal.testutils;

import com.amazonaws.serverless.proxy.internal.model.ApiGatewayAuthorizerContext;
import com.amazonaws.serverless.proxy.internal.model.ApiGatewayRequestContext;
import com.amazonaws.serverless.proxy.internal.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

/**
 * Request builder object. This is used by unit proxy to quickly create an AWS_PROXY request object
 */
public class AwsProxyRequestBuilder {

    //-------------------------------------------------------------
    // Variables - Private
    //-------------------------------------------------------------

    private AwsProxyRequest request;


    //-------------------------------------------------------------
    // Constructors
    //-------------------------------------------------------------

    public AwsProxyRequestBuilder() {
        this(null, null);
    }


    public AwsProxyRequestBuilder(String path) {
        this(path, null);
    }


    public AwsProxyRequestBuilder(String path, String httpMethod) {
        this.request = new AwsProxyRequest();
        this.request.setHttpMethod(httpMethod);
        this.request.setPath(path);
        this.request.setQueryStringParameters(new HashMap<>());
        this.request.setRequestContext(new ApiGatewayRequestContext());
        this.request.getRequestContext().setIdentity(new ApiGatewayRequestIdentity());
    }


    //-------------------------------------------------------------
    // Methods - Public
    //-------------------------------------------------------------

    public AwsProxyRequestBuilder method(String httpMethod) {
        this.request.setHttpMethod(httpMethod);
        return this;
    }


    public AwsProxyRequestBuilder path(String path) {
        this.request.setPath(path);
        return this;
    }


    public AwsProxyRequestBuilder json() {
        return this.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }


    public AwsProxyRequestBuilder form(String key, String value) {
        if (request.getHeaders() == null) {
            request.setHeaders(new HashMap<>());
        }
        request.getHeaders().put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
        String body = request.getBody();
        if (body == null) {
            body = "";
        }
        body += (body.equals("")?"":"&") + key + "=" + value;
        request.setBody(body);
        return this;
    }


    public AwsProxyRequestBuilder header(String key, String value) {
        if (this.request.getHeaders() == null) {
            this.request.setHeaders(new HashMap<>());
        }

        this.request.getHeaders().put(key, value);
        return this;
    }


    public AwsProxyRequestBuilder queryString(String key, String value) {
        if (this.request.getQueryStringParameters() == null) {
            this.request.setQueryStringParameters(new HashMap<>());
        }

        this.request.getQueryStringParameters().put(key, value);
        return this;
    }


    public AwsProxyRequestBuilder body(String body) {
        this.request.setBody(body);
        return this;
    }


    public AwsProxyRequestBuilder authorizerPrincipal(String principal) {
        this.request.getRequestContext().setAuthorizer(new ApiGatewayAuthorizerContext());
        this.request.getRequestContext().getAuthorizer().setPrincipalId(principal);
        return this;
    }


    public AwsProxyRequestBuilder cognitoUserPool(String identityId) {
        this.request.getRequestContext().getIdentity().setCognitoAuthenticationType("POOL");
        this.request.getRequestContext().getIdentity().setCognitoIdentityId(identityId);
        return this;
    }


    public AwsProxyRequestBuilder cognitoIdentity(String identityId, String identityPoolId) {
        this.request.getRequestContext().getIdentity().setCognitoAuthenticationType("IDENTITY");
        this.request.getRequestContext().getIdentity().setCognitoIdentityId(identityId);
        this.request.getRequestContext().getIdentity().setCognitoIdentityPoolId(identityPoolId);
        return this;
    }


    public AwsProxyRequestBuilder cookie(String name, String value) {
        if (request.getHeaders() == null) {
            request.setHeaders(new HashMap<>());
        }

        String cookies = request.getHeaders().get(HttpHeaders.COOKIE);
        if (cookies == null) {
            cookies = "";
        }

        cookies += (cookies.equals("")?"":"; ") + name + "=" + value;
        request.getHeaders().put(HttpHeaders.COOKIE, cookies);
        return this;
    }


    public AwsProxyRequest build() {
        return this.request;
    }
}
