/*
 * Copyright 2018 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package no.priv.bang.ukelonn.api.beans;

public class LoginResult {
    private String username = "";
    private String[] roles = {};
    private String errorMessage = "";

    public LoginResult() {
        // No-arg constructor for Jackson
    }

    public LoginResult(String username, String[] roles) {
        this.username = username;
        this.roles = roles;
    }

    public LoginResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUsername() {
        return username;
    }

    public String[] getRoles() {
        return roles;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
