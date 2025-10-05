/*
 */
package com.hhhh.group.secwealth.mktdata.common.filter.e2etrust;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.filter.e2etrust.properties.Parameters;
import com.hhhh.group.secwealth.mktdata.common.util.CryptoUtil;
import com.hhhh.group.secwealth.mktdata.common.util.DecryptPassword;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.PropertyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.wmd.itt.security.certificate.AggregatedCertificateLookup;
import com.hhhh.wmd.itt.security.certificate.CertificateLookup;
import com.hhhh.wmd.itt.security.certificate.FilteringCertificateLookup;
import com.hhhh.wmd.itt.security.certificate.KeystoreCertificateLookup;
import com.hhhh.wmd.itt.security.certificate.LdapCertificateLookup;
import com.hhhh.wmd.itt.security.filter.ChannelMappedFilter;
import com.hhhh.wmd.itt.security.filter.ChannelMappedFilter.ChannelMappedFilterBuilder;
import com.hhhh.wmd.itt.security.filter.DefaultHttpHeaderNames;
import com.hhhh.wmd.itt.security.filter.DefaultHttpRequestAttributeNames;
import com.hhhh.wmd.itt.security.filter.SamlAssertionParsingFilter;
import com.hhhh.wmd.itt.security.filter.TokenExistenceValidationFilter;
import com.hhhh.wmd.itt.security.filter.digital.E2ePolicyFilterBuilder;
import com.hhhh.wmd.itt.security.filter.opensaml.SamlGenerationFilter;
import com.hhhh.wmd.itt.security.filter.opensaml.SamlGenerationFilter.HttpParameterNames;
import com.hhhh.wmd.itt.security.filter.opensaml.SamlValidationFilter;
import com.hhhh.wmd.itt.security.filter.util.ErrorResponseUtil;
import com.hhhh.wmd.itt.security.filter.util.ErrorResponseUtilImpl;
import com.hhhh.wmd.itt.security.opensaml.CanonicalizationAlgorithm;
import com.hhhh.wmd.itt.security.opensaml.MessageDigestAlgorithm;
import com.hhhh.wmd.itt.security.opensaml.SamlBuilder;
import com.hhhh.wmd.itt.security.opensaml.SamlBuilder.SigningCredentialBuilder;
import com.hhhh.wmd.itt.security.opensaml.SamlValidator;
import com.hhhh.wmd.itt.security.opensaml.SamlValidator.CredentialBuilder;
import com.hhhh.wmd.itt.security.opensaml.SignatureAlgorithm;
import com.hhhh.wmd.itt.security.saml.DefaultSamlAssertionParser;


public class SecurityFilterAutoConfiguration {

    private PropertyUtil propertyUtil;
    private String configFile = "system/common/e2etrust/e2etrustConfig.properties";
    public static boolean e2eTrustEnable;

    private String geneartedSamlAttributeName = DefaultHttpRequestAttributeNames.GENERATED_SAML;
    private String sessionAttributeName = SamlGenerationFilter.DEFAULT_SESSION_ATTRIBUTE_NAME;
    private String httpParameterAttributePrefix = SamlGenerationFilter.DEFAULT_HTTP_PARAMETER_ATTRIBUTE_PREFIX;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RSA_SHA256;
    private MessageDigestAlgorithm messageDigestAlgorithm = MessageDigestAlgorithm.SHA256;
    private CanonicalizationAlgorithm canonicalizationAlgorithm = CanonicalizationAlgorithm.C14N_EXCL_OMIT_COMMENTS;
    private String rawSamlAttributeName = DefaultHttpRequestAttributeNames.RAW_SAML;
    private String samlAssertionAttributeName = DefaultHttpRequestAttributeNames.SAML_ASSERTION;
    private static final String[] DEFAULT_CHANNEL_LIST = {"OHB"};
    private List<String> channels =
        Collections.unmodifiableList(Arrays.asList(SecurityFilterAutoConfiguration.DEFAULT_CHANNEL_LIST));

    public void init() throws Exception {
        try {
            LogUtil.debug(SecurityFilterAutoConfiguration.class, "start to init filter.");
            initSecurityProperties();
            String e2eTrustEnableStr = this.propertyUtil.getProperty("e2etrust.enabled");
            if (StringUtil.isValid(e2eTrustEnableStr)) {
                SecurityFilterAutoConfiguration.e2eTrustEnable = Boolean.parseBoolean(e2eTrustEnableStr);
                LogUtil.info(SecurityFilterAutoConfiguration.class,
                    "e2e Trust Enable : " + SecurityFilterAutoConfiguration.e2eTrustEnable);
                if (SecurityFilterAutoConfiguration.e2eTrustEnable) {
                    samlGenerationFilterRegistration();
                    tokenExistenceValidationFilterRegistration();
                    samlParsingFilterRegistration();
                    channelMappedValidationFilterRegistration();
                    // generateSamlString();
                }
            }
        } catch (Exception e) {
            LogUtil.error(SecurityFilterAutoConfiguration.class, "Security Filter AutoConfiguration fail : " + e);
            throw new SystemException(e);
        }
    }

    private void tokenExistenceValidationFilterRegistration() {
        LogUtil.debug(SecurityFilterAutoConfiguration.class, "Start to init token Existence Validation Filter. ");
        ErrorResponseUtil errorResponseUtil = new ErrorResponseUtilImpl();
        Filter filter =
            TokenExistenceValidationFilter.builder().hhhhSamlTokenHeaderName(DefaultHttpHeaderNames.hhhh_SAML_HEADER_NAME)
                .hhhhSamlTokenHeaderName2(DefaultHttpHeaderNames.hhhh_SAML_HEADER_NAME_2)
                .rawSamlAttributeName(DefaultHttpRequestAttributeNames.RAW_SAML)
                .hhhhJwtHeaderName(DefaultHttpHeaderNames.hhhh_JWT_HEADER_NAME)
                .rawJwtAttributeName(DefaultHttpRequestAttributeNames.RAW_JWT).errorResponseUtil(errorResponseUtil).build();
        E2eTokenExistenceValidationFilter.setE2eTokenExistenceValidationFilter(filter);
    }

    private void channelMappedValidationFilterRegistration() throws Exception {
        LogUtil.debug(SecurityFilterAutoConfiguration.class, "start to init channel Mapped Validation Filter.");
        Filter digitalValidatorFilter = getE2eDigitalValidationFilter();
        Filter samlValidatorFilter = getE2eSamlValidationFilter();
        ChannelMappedFilterBuilder channelMappedFilterBuilder = ChannelMappedFilter.builder()
            .defaultDelegate(digitalValidatorFilter).samlAssertionAttributeName(this.samlAssertionAttributeName);

        List<String> openSamlChannels = this.channels;
        if (openSamlChannels != null) {
            for (String channel : openSamlChannels) {
                channelMappedFilterBuilder.delegate(channel, samlValidatorFilter);
            }
        }
        Filter filter = channelMappedFilterBuilder.build();
        E2eChannelMappedFilter.setE2eChannelMappedFilter(filter);
    }

    private Filter getE2eSamlValidationFilter() throws Exception {
        SamlValidationFilter filter = SamlValidationFilter.builder().rawSamlAttributeName(this.rawSamlAttributeName)
            .errorResponseUtil(new ErrorResponseUtilImpl()).samlValidator(samlValidator()).build();
        return filter;
    }

    private SamlValidator samlValidator() throws Exception {

        String publicKeyFile = this.propertyUtil.getProperty("staff_channel.publicKeyFile");
        String encryptKeyFile = this.propertyUtil.getProperty("staff_channel.encryptKeyFile");
        String keyStorePasswordFile = this.propertyUtil.getProperty("staff_channel.keyStorePasswordFile");

        String wealthValidationKeystorePassword = DecryptPassword.decrypt(publicKeyFile, encryptKeyFile, keyStorePasswordFile);
        // LogUtil.debug(SecurityFilterAutoConfiguration.class,
        // "wealthValidationKeystorePassword is : " +
        // wealthValidationKeystorePassword);

        com.hhhh.wmd.itt.security.certificate.KeystoreCertificateLookup openSamlValidationCertificateLookup =
            new com.hhhh.wmd.itt.security.certificate.KeystoreCertificateLookup();
        openSamlValidationCertificateLookup.setKeyStoreType(this.propertyUtil.getProperty("staff_channel.keyStoreType"));
        openSamlValidationCertificateLookup.setKeyStorePath(this.propertyUtil.getProperty("staff_channel.keyStoreFile"));
        openSamlValidationCertificateLookup.setKeyStorePassword(wealthValidationKeystorePassword.toCharArray());
        openSamlValidationCertificateLookup
            .setKeyStoreCertificateAlias(this.propertyUtil.getProperty("staff_channel.keyStoreAlias"));

        CredentialBuilder credentialBuilder = new CredentialBuilder().certificateLookup(openSamlValidationCertificateLookup);

        SamlValidator samlValidator = new SamlValidator();
        samlValidator.setCredentialBuilder(credentialBuilder);
        samlValidator.setNotBeforeTolerance(Long.parseLong(this.propertyUtil.getProperty("staff_channel.secondsValid")));
        return samlValidator;
    }

    private Filter getE2eDigitalValidationFilter() throws Exception {
        Filter filter = E2ePolicyFilterBuilder.aFilter().withErrorResponseUtil(new ErrorResponseUtilImpl())
            .withCertificateLookup(digitalCertificateLookup()).build();
        return filter;
    }

    private CertificateLookup digitalCertificateLookup() throws Exception {
        AggregatedCertificateLookup certificateLookup = new AggregatedCertificateLookup();
        certificateLookup.append(ldapCertificateLookup());
        certificateLookup.append(keyStoreCertificateLookup());
        return certificateLookup;
    }

    private CertificateLookup ldapCertificateLookup() throws Exception {
        String ldapProvider = this.propertyUtil.getProperty("e2etrust.customer.ldapProvider");
        String ldapUsername = this.propertyUtil.getProperty("e2etrust.customer.ldapUsername");

        String ldapPassword = this.decryptPwd(this.propertyUtil.getProperty("e2etrust.customer.ldapPassword.encryptKey", ""),
            this.propertyUtil.getProperty("e2etrust.customer.ldapPassword", ""),
            this.propertyUtil.getProperty("e2etrust.customer.ldapPassword.DESede", ""));
        String ldapConnectionTimeout = this.propertyUtil.getProperty("e2etrust.customer.ldapTimeout");
        String ldapAlias = this.propertyUtil.getProperty("e2etrust.customer.ldapAlias");
        CertificateLookup certificateLookup =
            new LdapCertificateLookup(ldapProvider, ldapUsername, ldapPassword, ldapConnectionTimeout);
        List<String> keyAliases = null;
        if (StringUtils.isNotBlank(ldapAlias)) {
            keyAliases = Arrays.asList(ldapAlias.split(","));
        }
        if (CollectionUtils.isEmpty(keyAliases)) {
            return certificateLookup;
        } else {
            return new FilteringCertificateLookup(certificateLookup, keyAliases);
        }
    }

    private CertificateLookup keyStoreCertificateLookup() throws Exception {
        KeystoreCertificateLookup certificateLookup = new KeystoreCertificateLookup();

        String publicKeyFile = this.propertyUtil.getProperty("e2etrust.customer.publicKeyFile");
        String keyStoreType = this.propertyUtil.getProperty("e2etrust.customer.keyStoreType");
        String keyStoreFile = this.propertyUtil.getProperty("e2etrust.customer.keyStoreFile");
        String keyStoreAlias = this.propertyUtil.getProperty("e2etrust.customer.keyStoreAlias");
        String encryptKeyFile = this.propertyUtil.getProperty("e2etrust.customer.encryptKeyFile");
        String keyStorePasswordFile = this.propertyUtil.getProperty("e2etrust.customer.keyStorePasswordFile");

        String wealthValidationKeystorePassword = DecryptPassword.decrypt(publicKeyFile, encryptKeyFile, keyStorePasswordFile);
        // LogUtil.debug(SecurityFilterAutoConfiguration.class,
        // "wealthValidationKeystorePassword is : " +
        // wealthValidationKeystorePassword);

        certificateLookup.setKeyStoreType(keyStoreType);
        certificateLookup.setKeyStorePath(keyStoreFile);
        certificateLookup.setKeyStoreCertificateAlias(keyStoreAlias);
        certificateLookup.setKeyStorePassword(wealthValidationKeystorePassword.toCharArray());
        return certificateLookup;
    }

    private void samlParsingFilterRegistration() throws Exception {
        LogUtil.debug(SecurityFilterAutoConfiguration.class, "init saml Parsing Filter. ");
        ErrorResponseUtil errorResponseUtil = new ErrorResponseUtilImpl();
        DefaultSamlAssertionParser samlAssertionParser = new DefaultSamlAssertionParser();

        SamlAssertionParsingFilter filter = SamlAssertionParsingFilter.builder().errorResponseUtil(errorResponseUtil)
            // .hhhhSamlTokenHeaderName(DefaultHttpHeaderNames.hhhh_SAML_HEADER_NAME)
            // .hhhhSamlTokenHeaderName2(DefaultHttpHeaderNames.hhhh_SAML_HEADER_NAME_2)
            .parser(samlAssertionParser).rawSamlAttributeName(DefaultHttpRequestAttributeNames.RAW_SAML)
            .samlAssertionAttributeName(DefaultHttpRequestAttributeNames.SAML_ASSERTION).build();
        E2eSamlAssertionParsingFilter.setE2eSamlAssertionParsingFilter(filter);
    }


    private void initSecurityProperties() throws Exception {
        LogUtil.debug(SecurityFilterAutoConfiguration.class, "init Security Properties.");
        this.propertyUtil = new PropertyUtil(this.configFile);
    }

    private void samlGenerationFilterRegistration() throws Exception {
        LogUtil.debug(SecurityFilterAutoConfiguration.class, "init saml Generation Filter. ");

        // Generation generationProps = new Generation();
        // Signature signatureProps = new Signature();
        // AssertionEncryption assertionEncryptionProps = new
        // AssertionEncryption();
        Parameters parametersProps = new Parameters();

        String publicKeyFile = this.propertyUtil.getProperty("opensaml.generation.publicKeyFile");
        String encryptKeyFile = this.propertyUtil.getProperty("opensaml.generation.encryptKeyFile");
        String keyStorePasswordFile = this.propertyUtil.getProperty("opensaml.generation.keyStorePasswordFile");
        String keyStoreSignerKeyPasswordFile = this.propertyUtil.getProperty("opensaml.generation.keyStoreSignerKeyPasswordFile");

        String wealthSignerKeystorePassword = DecryptPassword.decrypt(publicKeyFile, encryptKeyFile, keyStorePasswordFile);
        String wealthSignerKeystoreSignerKeypassword =
            DecryptPassword.decrypt(publicKeyFile, encryptKeyFile, keyStoreSignerKeyPasswordFile);

        // LogUtil.debug(SecurityFilterAutoConfiguration.class,
        // "wealthSignerKeystorePassword is : " +
        // wealthSignerKeystorePassword);
        // LogUtil.debug(SecurityFilterAutoConfiguration.class,
        // "wealthSignerKeystoreSignerKeypassword is : " +
        // wealthSignerKeystoreSignerKeypassword);

        char[] keyStorePassword = wealthSignerKeystorePassword.toCharArray();
        char[] keyPassword = wealthSignerKeystoreSignerKeypassword.toCharArray();

        SigningCredentialBuilder signingCredentialBuilder =
            new SigningCredentialBuilder().keyStoreType(this.propertyUtil.getProperty("opensaml.generation.keyStoreType"))
                .keyStorePath(this.propertyUtil.getProperty("opensaml.generation.keyStoreFile")).keyStorePassword(keyStorePassword)
                .keyAlias(this.propertyUtil.getProperty("opensaml.generation.keyStoreAlias")).keyPassword(keyPassword);
        // .provider();

        HttpParameterNames httpParameterNames = HttpParameterNames.builder().appId(parametersProps.getAppId())
            .cam(parametersProps.getCam()).channelId(parametersProps.getChannelId()).companyId(parametersProps.getCompanyId())
            .delegateId(parametersProps.getDelegateId()).deviceId(parametersProps.getDeviceId()).guid(parametersProps.getGuid())
            .ip(parametersProps.getIp()).ipId(parametersProps.getIpId()).nameId(parametersProps.getNameId())
            .roles(parametersProps.getRoles()).userId(parametersProps.getUserId())
            // .authenticationContext(parametersProps.getAuthenticationContext())
            // .customAttributes(parametersProps.getCustomAttributes())
            .build();

        String samlNotBeforeThreshold = this.propertyUtil.getProperty("opensaml.generation.samlNotBeforeThreshold");
        String samlValidDuration = this.propertyUtil.getProperty("opensaml.generation.samlValidDuration");
        int notBeforeTolerance = Integer.parseInt(samlNotBeforeThreshold);
        int validDuration = Integer.parseInt(samlValidDuration);

        String samlKeyIssuer = this.propertyUtil.getProperty("opensaml.generation.samlKeyIssuer");
        String keyStoreAlias = this.propertyUtil.getProperty("opensaml.generation.keyStoreAlias");

        SamlGenerationFilter filter = SamlGenerationFilter.builder().geneartedSamlAttributeName(this.geneartedSamlAttributeName)
            .sessionAttributeName(this.sessionAttributeName).httpParameterAttributePrefix(this.httpParameterAttributePrefix)
            .httpParameterNames(httpParameterNames).signatureAlgorithm(this.signatureAlgorithm)
            .messageDigestAlgorithm(this.messageDigestAlgorithm).canonicalizationAlgorithm(this.canonicalizationAlgorithm)
            .signingCredentialBuilder(signingCredentialBuilder).notBeforeTolerance(notBeforeTolerance).validDuration(validDuration)
            // .audienceUriList(this.audienceUris)
            // .authenticationContext(this.authenticationContext)
            // .nameIdNameQualifier("nameIdNameQualifier")
            // .nameIdSpNameQualifier("nameIdSpNameQualifier")
            // use key alias by default, unless it is configured
            // .issuer(StringUtils.defaultIfBlank(samlKeyIssuer,
            // keyStoreAlias))
            .issuer(keyStoreAlias)
            // .recipient("recipient")
            // .assertionOnly(false)
            // .keyInfoType("keyInfoType")
            // .attributesMap(this.attributes)
            // .assertionEncryption(false)
            // .assertionKeyTransportAlgorithm("keyTransportAlgorithm")
            // .assertionEncryptionAlgorithm()
            // .keyEncryptionCredentialBuilder()
            .build();

        E2eSamlGenerationFilter.setE2eSamlGenerationFilter(filter);
    }

    private String generateSamlString() throws Exception {
        String publicKeyFile = this.propertyUtil.getProperty("staff_channel.publicKeyFile");
        String encryptKeyFile = this.propertyUtil.getProperty("staff_channel.encryptKeyFile");
        String keyStorePasswordFile = this.propertyUtil.getProperty("staff_channel.keyStorePasswordFile");
        String samlKeyIssuer = this.propertyUtil.getProperty("staff_channel.samlKeyIssuer");
        String keyStoreAlias = this.propertyUtil.getProperty("staff_channel.keyStoreAlias");
        String nameId = this.propertyUtil.getProperty("staff_channel.nameId");
        String samlNotBeforeThreshold = this.propertyUtil.getProperty("staff_channel.samlNotBeforeThreshold");
        String samlValidDuration = this.propertyUtil.getProperty("staff_channel.samlValidDuration");
        int notBeforeTolerance = Integer.parseInt(samlNotBeforeThreshold);
        int validDuration = Integer.parseInt(samlValidDuration);

        String wealthSignerKeystorePassword = DecryptPassword.decrypt(publicKeyFile, encryptKeyFile, keyStorePasswordFile);
        String wealthSignerKeystoreSignerKeypassword = DecryptPassword.decrypt(publicKeyFile, encryptKeyFile, keyStorePasswordFile);

        char[] keyStorePassword = wealthSignerKeystorePassword.toCharArray();
        char[] keyPassword = wealthSignerKeystoreSignerKeypassword.toCharArray();

        SigningCredentialBuilder signingCredentialBuilder = new SigningCredentialBuilder().keyStoreType("JKS")
            .keyStorePath(this.propertyUtil.getProperty("staff_channel.keyStoreFile")).keyStorePassword(keyStorePassword)
            .keyAlias(this.propertyUtil.getProperty("staff_channel.keyStoreAlias")).keyPassword(keyPassword);

        final SamlBuilder samlBuilder = new SamlBuilder().notBeforeThreshold(notBeforeTolerance).validDuration(validDuration)
            .signatureAlgorithm(SignatureAlgorithm.RSA_SHA256).messageDigestAlgorithm(MessageDigestAlgorithm.SHA256)
            .canonicalizationAlgorithm(CanonicalizationAlgorithm.C14N_EXCL_OMIT_COMMENTS)
            .signingCredentialBuilder(signingCredentialBuilder).nameId(nameId)
            .issuer(StringUtils.defaultIfBlank(samlKeyIssuer, keyStoreAlias));

        samlBuilder.attribute("ChannelID", "OHB");

        String samlString = samlBuilder.build();
        // LogUtil.debug(SecurityFilterAutoConfiguration.class, "Generate
        // samlString is : " + samlString);
        return samlString;
    }

    private String decryptPwd(final String keyPath, final String encryptedPwd, final String DESedePath) throws Exception {
        return CryptoUtil.decryptPwd(keyPath, encryptedPwd, DESedePath);
    }
}