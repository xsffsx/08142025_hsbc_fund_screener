package com.dummy.wmd.wpc.graphql.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;

import com.dummy.wmd.wpc.graphql.model.UserInfo;
import org.springframework.stereotype.Service;

@SuppressWarnings({"java:S1149"})
@Slf4j
@Service
public class LdapService {

	@Value("${product.ldap.provider.url}")
	private String providerUrl;

	@Value("${product.ldap.security.credentials}")
	private String credentials;

	@Value("${product.ldap.security.principal}")
	private String principal;

	@Value("${product.ldap.base}")
	private String base;

	@Cacheable("productCache")
	public UserInfo getLdapUserById(String staffId) throws NamingException {
		// preventing LDAP injection
		if (Objects.isNull(staffId) || !staffId.matches("\\d{8,}")) {
			return null;
		}
		
		Hashtable<String, String> env = new Hashtable<>();
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("java.naming.provider.url", providerUrl); //ldap://aa-lds-test.uk.dummy:3269
		env.put("java.naming.security.authentication", "simple");
		env.put("java.naming.security.credentials", credentials);
		env.put("java.naming.security.principal", principal); //CN=HBAP-HKGLUEWPCA003,OU=Service Accounts,DC=InfoDir,DC=Dev,DC=dummy
		env.put("java.naming.security.protocol", "ssl");

		LdapContext ctx = new InitialLdapContext(env, null);
		return buildLdapUser(staffId, ctx);
	}

	private UserInfo buildLdapUser(String staffId, LdapContext ctx) throws NamingException {
		// initial an empty String for user name; meanwhile, an empty List for
		// user group.
		String tempUserName = "";
		List<String> tempUserGroup = new ArrayList<>();
		UserInfo ldapUser = new UserInfo(staffId, tempUserName, tempUserGroup);
		SearchControls searchCtls = new SearchControls();

		String[] arr = new String[3];
		arr[0] = "EmployeeID";
		arr[1] = "MemberOf";
		arr[2] = "displayName";

		searchCtls.setReturningAttributes(arr);
		String filter = String.format("(&(|(objectClass=userProxy)(objectClass=user))(|(EmployeeID=%s)))", sanitize(staffId));
		// BASE = "CN=HBAP-HKGLUEWPCA003,OU=Service Accounts,DC=InfoDir,DC=Dev,DC=dummy"
		NamingEnumeration<SearchResult> results = ctx.search(base, filter, searchCtls);
		if (results != null) {
			while (results.hasMoreElements()) {
				SearchResult sr = results.nextElement();
				if (sr != null && sr.getAttributes() != null) {
					Attribute attr0 = sr.getAttributes().get("EmployeeID");
					if (attr0 != null && attr0.get() != null) {
						log.info("EmployeeID: " + attr0.get());
					}
					ldapUser.setGroups(getTempUserGroup(sr.getAttributes().get("MemberOf")));
					ldapUser.setName(getTempUserName(sr.getAttributes().get("displayName")));
				}
			}
		}
		return ldapUser;
	}

	private List<String> getTempUserGroup(Attribute attr1) throws NamingException {
		List<String> tempUserGroup = new ArrayList<>();
		if (attr1 != null && attr1.getAll() != null) {
			NamingEnumeration<?> attrList = attr1.getAll();
			while (attrList.hasMoreElements()) {
				Object obj = attrList.nextElement();
				if (obj != null) {
					String attr = obj.toString().trim();
					tempUserGroup.add(attr);
					log.debug("current attr(MemberOf): " + attr);
				}
			}
		}
		return tempUserGroup;
	}

	private String getTempUserName(Attribute attr2) throws NamingException {
		String tempUserName = "";
		if (attr2 != null && attr2.get() != null) {
			tempUserName = ((String) attr2.get()).trim();
			log.info("Employee display name: " + attr2.get());
		}
		return tempUserName;
	}

	// so that can pass checkmarx checking
	private String sanitize(String staffId) {
		Long num = Long.parseUnsignedLong(staffId);
		String s = num.toString();
		// 0 prefix staff id has problem, need special handling eg. 0123456 will be turned to 123456
		s = Strings.padStart(s, staffId.length(), '0');
		return s;
	}
}
