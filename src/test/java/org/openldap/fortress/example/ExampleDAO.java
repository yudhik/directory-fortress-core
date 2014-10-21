/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.openldap.fortress.example;

import org.slf4j.LoggerFactory;
import org.openldap.fortress.CreateException;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.RemoveException;
import org.openldap.fortress.UpdateException;
import org.openldap.fortress.cfg.Config;
import org.openldap.fortress.ldap.UnboundIdDataProvider;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleDAO extends UnboundIdDataProvider

{
    private static final String CLS_NM = ExampleDAO.class.getName();
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( CLS_NM );
    protected final static String[] EXAMPLE_ATRS = {
        GlobalIds.FT_IID, EIds.EXAMPLE_NM, GlobalIds.DESC, GlobalIds.CONSTRAINT
    };

    /**
     * @param entity
     * @return
     * @throws org.openldap.fortress.CreateException
     *
     */
    public Example create(Example entity)
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + entity.getName() + "," + Config.getProperty(EIds.EXAMPLE_ROOT);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("create dn [" + dn + "]");
        }
        try
        {

            /*
        public class Example
                implements Constraint, java.io.Serializable
        {
            private String id;          // this maps to oamId
            private String name;          // this is oamRoleName
            private String description; // this is description
            private String dn;          // this attribute is automatically saved to each ldap record.
            private String beginTime;     // this attribute is oamBeginTime
            private String endTime;        // this attribute is oamEndTime
            private String beginDate;    // this attribute is oamBeginDate
            private String endDate;        // this attribute is oamEndDate
            private String beginLockDate;// this attribute is oamBeginLockDate
            private String endLockDate;    // this attribute is oamEndLockDate
            private String dayMask;        // this attribute is oamDayMask
            private int timeout;        // this attribute is oamTimeOut
            */

            ld = getAdminConnection();
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add(createAttributes(GlobalIds.OBJECT_CLASS, EIds.EXAMPLE_OBJ_CLASS));
            entity.setId();
            attrs.add(createAttribute(GlobalIds.FT_IID, entity.getId()));
            attrs.add(createAttribute(EIds.EXAMPLE_NM, entity.getName()));
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
                attrs.add(createAttribute(GlobalIds.DESC, entity.getDescription()));
            // organizational name requires CN attribute:
            attrs.add(createAttribute(GlobalIds.CN, entity.getName()));

            //AttrHelper.loadTemporalAttrs(entity, attrs);
            entity.setName("EXAMPLE");
            attrs.add(createAttribute(GlobalIds.CONSTRAINT, org.openldap.fortress.util.time.CUtil.setConstraint(entity)));

            LDAPEntry myEntry = new LDAPEntry(dn, attrs);
            add(ld, myEntry);
        }
        catch (LDAPException e)
        {
            String error = "create [" + entity.getName() + "] caught LDAPException=" + e;
            LOG.error(error);
            throw new CreateException(EErrIds.EXAMPLE_ADD_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws org.openldap.fortress.UpdateException
     *
     */
    public Example update(Example entity)
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + entity.getName() + "," + org.openldap.fortress.cfg.Config.getProperty(EIds.EXAMPLE_ROOT);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("update dn [" + dn + "]");
        }
        try
        {
            ld = getAdminConnection();
            LDAPModificationSet mods = new LDAPModificationSet();
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
            {
                LDAPAttribute desc = new LDAPAttribute(GlobalIds.DESC, entity.getDescription());
                mods.add(LDAPModification.REPLACE, desc);
            }
            String szRawData = org.openldap.fortress.util.time.CUtil.setConstraint(entity);
            if (szRawData != null && szRawData.length() > 0)
            {
                LDAPAttribute constraint = new LDAPAttribute(GlobalIds.CONSTRAINT, szRawData);
                mods.add(LDAPModification.REPLACE, constraint);
            }
            if (mods.size() > 0)
            {
                modify(ld, dn, mods);
            }
        }
        catch (LDAPException e)
        {
            String error = "update [" + entity.getName() + "] caught LDAPException=" + e;
            LOG.error(error);
            throw new UpdateException(EErrIds.EXAMPLE_UPDATE_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return entity;
    }


    /**
     * @param name
     * @throws org.openldap.fortress.RemoveException
     *
     */
    public void remove(String name)
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + name + "," + Config.getProperty(EIds.EXAMPLE_ROOT);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("remove dn [" + dn + "]");
        }
        try
        {
            ld = getAdminConnection();
            delete(ld, dn);
        }
        catch (LDAPException e)
        {
            String error = "remove [" + name + "] caught LDAPException=" + e;
            LOG.error(error);
            throw new RemoveException(EErrIds.EXAMPLE_DELETE_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
    }


    /**
     * @param name
     * @return
     * @throws org.openldap.fortress.FinderException
     *
     */
    public Example findByKey(String name)
        throws org.openldap.fortress.FinderException
    {
        Example entity = null;
        LDAPConnection ld = null;
        String dn = GlobalIds.CN + "=" + name + "," + Config.getProperty(EIds.EXAMPLE_ROOT);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("findByKey dn [" + dn + "]");
        }
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read(ld, dn, EXAMPLE_ATRS);
            entity = getEntityFromLdapEntry(findEntry);
            if (entity == null)
            {
                String error = "findByKey could not find entry for example name [" + name + "]";
                LOG.error(error);
                throw new org.openldap.fortress.FinderException(EErrIds.EXAMPLE_NOT_FOUND, error);
            }
        }
        catch (LDAPException e)
        {
            if (e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT)
            {
                String error = "findByKey COULD NOT FIND ENTRY for example name [" + name + "]";
                LOG.warn(error);
                throw new org.openldap.fortress.FinderException(EErrIds.EXAMPLE_NOT_FOUND, error);
            }
            else
            {
                String error = "findByKey name [" + name + "] caught LDAPException=" + e;
                LOG.warn(error);
                throw new org.openldap.fortress.FinderException(EErrIds.EXAMPLE_READ_FAILED, error);
            }
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return entity;
    }


    /**
     * @param searchVal
     * @return
     * @throws org.openldap.fortress.FinderException
     *
     */
    public List<Example> findExamples(String searchVal)
        throws org.openldap.fortress.FinderException
    {
        List<Example> exampleList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String exampleRoot = org.openldap.fortress.cfg.Config.getProperty(EIds.EXAMPLE_ROOT);

        if (LOG.isDebugEnabled())
        {
            LOG.debug("findExamples: " + EIds.EXAMPLE_ROOT + " [" + exampleRoot + "]");
        }
        try
        {
            searchVal = encodeSafeText(searchVal, GlobalIds.ROLE_LEN);
            ld = getAdminConnection();
            String filter = GlobalIds.FILTER_PREFIX + Arrays.toString(EIds.EXAMPLE_OBJ_CLASS) + ")("
                + EIds.EXAMPLE_NM + "=" + searchVal + "*))";
            searchResults = search(ld, exampleRoot,
                LDAPConnection.SCOPE_ONE, filter, EXAMPLE_ATRS, false, EIds.BATCH_SIZE);
            while (searchResults.hasMoreElements())
            {
                exampleList.add(getEntityFromLdapEntry(searchResults.next()));
            }
        }
        catch (LDAPException e)
        {
            String error = "findExamples caught LDAPException=" + e;
            LOG.warn(error);
            throw new org.openldap.fortress.FinderException(EErrIds.EXAMPLE_SEARCH_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return exampleList;
    }


    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private Example getEntityFromLdapEntry(LDAPEntry le)
    {
        /*
        public class Role
                implements Constraint, java.io.Serializable
        {
            private String id;          // this maps to oamId
            private String name;          // this is oamRoleName
            private String description; // this is description
            private String dn;          // this attribute is automatically saved to each ldap record.
            private String beginTime;     // this attribute is oamBeginTime
            private String endTime;        // this attribute is oamEndTime
            private String beginDate;    // this attribute is oamBeginDate
            private String endDate;        // this attribute is oamEndDate
            private String beginLockDate;// this attribute is oamBeginLockDate
            private String endLockDate;    // this attribute is oamEndLockDate
            private String dayMask;        // this attribute is oamDayMask
            private int timeout;        // this attribute is oamTimeOut
            */
        Example entity = new Example();
        entity.setId(getAttribute(le, GlobalIds.FT_IID));
        entity.setName(getAttribute(le, EIds.EXAMPLE_NM));
        entity.setDescription(getAttribute(le, GlobalIds.DESC));
        unloadTemporal(le, entity);
        return entity;
    }
}

