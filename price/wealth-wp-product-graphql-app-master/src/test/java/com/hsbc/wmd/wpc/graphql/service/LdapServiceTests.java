package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.model.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class LdapServiceTests {

    @InjectMocks
    private LdapService ldapService;
    @Mock
    private NamingEnumeration<SearchResult> results;
    @Mock
    private SearchResult searchResult;
    @Mock
    private Attributes attrs;
    @Mock
    private Attribute attr;
    @Mock
    private NamingEnumeration attrList;

    @Before
    public void setUp() {
        ldapService = new LdapService();
        ReflectionTestUtils.setField(ldapService, "base", "simple");
        ReflectionTestUtils.setField(ldapService, "providerUrl", "ldap://aa-lds-test.uk.dummy:3269");
        ReflectionTestUtils.setField(ldapService, "credentials", "Password123456");
        ReflectionTestUtils.setField(ldapService, "principal", "CN=HBAP-HKGLUEWPCA003,OU=Service Accounts,DC=InfoDir,DC=Dev,DC=dummy");
    }

    @Test
    public void testGetLdapUserById_givenStaffId_returnsUserInfo() {
        try {
            MockedConstruction<InitialLdapContext> mockedConstruction = Mockito.mockConstruction(InitialLdapContext.class, (ctx, context) ->{
                Mockito.when(ctx.search(anyString(), anyString(), any(SearchControls.class))).thenReturn(results);
                Mockito.when(results.hasMoreElements()).thenReturn(true).thenReturn(false);
                Mockito.when(results.nextElement()).thenReturn(searchResult);
                Mockito.when(searchResult.getAttributes()).thenReturn(attrs);
                Mockito.when(attrs.get(anyString())).thenReturn(attr);
                Mockito.when(attr.getAll()).thenReturn(attrList);
                Mockito.when(attrList.hasMoreElements()).thenReturn(true).thenReturn(false);
                Mockito.when(attrList.nextElement()).thenReturn("dummy");
                Mockito.when(attr.get()).thenReturn("Employee");
            });
            UserInfo userInfo = ldapService.getLdapUserById("84034240");
            Assert.assertNotNull(userInfo);
            UserInfo userInfo_2 = ldapService.getLdapUserById("8403420");
            Assert.assertNull(userInfo_2);
            mockedConstruction.close();
        } catch (NamingException e) {
        }
    }
}
