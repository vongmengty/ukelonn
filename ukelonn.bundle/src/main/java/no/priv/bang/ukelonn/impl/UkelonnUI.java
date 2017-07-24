/*
 * Copyright 2016-2017 Steinar Bang
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
package no.priv.bang.ukelonn.impl;

import javax.servlet.http.Cookie;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

@Theme("touchkit")
@Widgetset("com.vaadin.addon.touchkit.gwt.TouchKitWidgetSet")
public class UkelonnUI extends UI {
    private static final long serialVersionUID = 1388525490129647161L;
    private UkelonnServletProvider provider;

    public UkelonnUI(UkelonnServletProvider ukelonnServletProvider) {
        this.provider = ukelonnServletProvider;
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Ukelønn");
        setNavigator(new Navigator(this, this));
        Cookie uiStyle = checkForUIStyleCookie(request);
        if (isLogout(request)) {
            SecurityUtils.getSubject().logout();
            getPage().setLocation(getPage().getLocation().resolve(".")); // Clear the "?logout=yes" argument from the URL
        }

        // Add all of the different views
        if (isMobile(uiStyle)) {
            getNavigator().addView("", new UserView(provider, request));
            getNavigator().addView("admin", new AdminView(provider, request));
        } else {
            setTheme("valo");
            getNavigator().addView("", new UserFallbackView(provider, request));
            getNavigator().addView("admin", new AdminFallbackView(provider, request));
        }
        getNavigator().addView("login", new LoginView(provider, request, getNavigator()));
        if (!isLoggedIn()) {
            getNavigator().navigateTo("login");
        } else if (isAdministrator()) {
            getNavigator().navigateTo("admin");
        } else {
            getNavigator().navigateTo("");
        }
    }

    private boolean isLogout(VaadinRequest request) {
        // Only an explicit ?logout=yes argument will cause a logout
        if ("yes".equals(request.getParameter("logout"))) {
            return true;
        }

        return false;
    }

    private boolean isMobile(Cookie uiStyle) {
        // The default is mobile.  Only when explicitly set to browser
        // will the browser UI be used.
        if (uiStyle != null) {
            if ("ui-style".equals(uiStyle.getName()) && "browser".equals(uiStyle.getValue())) {
                return false;
            }
        }

        return true;
    }

    protected boolean isAdministrator() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.hasRole("administrator");
    }

    protected boolean isLoggedIn() {
        Subject currentUser = SecurityUtils.getSubject();
        boolean isRemembered = currentUser.isRemembered();
        boolean isAuthenticated = currentUser.isAuthenticated();
        return isRemembered || isAuthenticated;
    }

    private Cookie checkForUIStyleCookie(VaadinRequest request) {
        Cookie uiStyle = getCookieByName(request, "ui-style");

        // The URI request parameter "ui-style" can be used to switch between UIs
        String uiStyleParam = request.getParameter("ui-style");
        if (uiStyleParam != null) {
            if ("browser".equals(uiStyleParam)) {
                uiStyle = new Cookie("ui-style", "browser");
            } else {
                uiStyle = new Cookie("ui-style", "mobile");
            }

            // Save cookie with updated value
            uiStyle.setPath(request.getContextPath());
            VaadinService.getCurrentResponse().addCookie(uiStyle);
            getPage().setLocation(getPage().getLocation().resolve(".")); // Clear the "?ui-style=..." argument from the URL
        }
        return uiStyle;
    }

    private Cookie getCookieByName(VaadinRequest request, String name) {
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
}
