package com.florianbuchner.keycloakcustom.login;

import org.keycloak.authentication.AbstractFormAuthenticator;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class CaptchaAuthenticator implements Authenticator {

    static final String FORM = "custom_captcha.ftl";
    static final String CAPTCHA_ATTRIBUTE = "CAPTCHA";
    static final String FORM_CAPTCHA = "captcha";
    static final String FORM_USER_CAPTCHA = "user_captcha";

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        UserModel user = authenticationFlowContext.getUser();
        String captcha = generateCaptcha();
        user.setSingleAttribute(CAPTCHA_ATTRIBUTE, captcha);
        LoginFormsProvider form = authenticationFlowContext.form();
        form.setAttribute(FORM_CAPTCHA, captcha);
        Response challenge = form.createForm(FORM);
        authenticationFlowContext.challenge(challenge);
    }

    boolean isCaptchaCorrect(final String captcha, final MultivaluedMap<String, String> inputData) {
        return captcha != null && captcha.equals(inputData.getFirst(FORM_USER_CAPTCHA));
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        UserModel user = authenticationFlowContext.getUser();

        if (isCaptchaCorrect(user.getFirstAttribute(CAPTCHA_ATTRIBUTE), authenticationFlowContext.getHttpRequest().getFormParameters())) {
            authenticationFlowContext.success();
        }
        else {
            String captcha = generateCaptcha();
            user.removeAttribute(CAPTCHA_ATTRIBUTE);
            user.setSingleAttribute(CAPTCHA_ATTRIBUTE, captcha);
            LoginFormsProvider form = authenticationFlowContext.form();
            form.setAttribute(FORM_CAPTCHA, captcha);
            form.setError("invalidCaptcha");
            Response challenge = form.createForm(FORM);
            authenticationFlowContext.challenge(challenge);
        }
    }

    String generateCaptcha() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }
}
