<#import "template.ftl" as layout>
<@layout.registrationLayout displayWide=(realm.password); section>
    <#if section = "header">
        ${msg("enterCaptcha")}
        <div class="login-logo"/>
    <#elseif section = "form">
        <div id="kc-form">
            <div id="kc-form-wrapper">
                <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">

                    <div class="${properties.kcFormGroupClass!}">
                        <label for="username" class="${properties.kcLabelClass!}">${msg("captcha")}</label>
                        <span>${captcha}</span>
                    </div>

                    <div class="${properties.kcFormGroupClass!}">
                        <label for="username" class="${properties.kcLabelClass!}">${msg("enterCaptcha")}</label>
                        <input tabindex="1" id="username" class="${properties.kcInputClass!}" name="user_captcha" type="text" autofocus autocomplete="off" />
                    </div>

                    <div id="kc-form-buttons" class="${properties.kcFormGroupClass!}">
                        <input tabindex="4" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="${msg("doLogIn")}"/>
                    </div>
                </form>
            </div>
        </div>
    </#if>

</@layout.registrationLayout>
