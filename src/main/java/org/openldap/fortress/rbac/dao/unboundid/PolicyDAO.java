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
package org.openldap.fortress.rbac.dao.unboundid;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.openldap.fortress.CreateException;
import org.openldap.fortress.FinderException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.ObjectFactory;
import org.openldap.fortress.RemoveException;
import org.openldap.fortress.UpdateException;
import org.openldap.fortress.ldap.UnboundIdDataProvider;
import org.openldap.fortress.rbac.PwPolicy;
import org.openldap.fortress.util.attr.VUtil;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;


/**
 * This DAO class maintains the OpenLDAP Password Policy entity which is a composite of the following structural and aux object classes:
 * <h4>1. organizationalRole Structural Object Class is used to store basic attributes like cn and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code> objectclass ( 2.5.6.14 NAME 'device'</code>
 * <li> <code>DESC 'RFC2256: a device'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( serialNumber $ seeAlso $ owner $ ou $ o $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. pwdPolicy AUXILIARY Object Class is used to store OpenLDAP Password Policies</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.42.2.27.8.2.1</code>
 * <li> <code>NAME 'pwdPolicy'</code>
 * <li> <code>SUP top</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MUST ( pwdAttribute )</code>
 * <li> <code>MAY ( pwdMinAge $ pwdMaxAge $ pwdInHistory $ pwdCheckQuality $</code>
 * <li> <code>pwdMinLength $ pwdExpireWarning $ pwdGraceAuthNLimit $ pwdLockout $</code>
 * <li> <code>pwdLockoutDuration $ pwdMaxFailure $ pwdFailureCountInterval $</code>
 * <li> <code>pwdMustChange $ pwdAllowUserChange $ pwdSafeModify ) )</code>
 * <li> <code></code>
 * <li> <code></code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
 * <ul>
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
public final class PolicyDAO extends UnboundIdDataProvider implements org.openldap.fortress.rbac.dao.PolicyDAO
{
    /*
      *  *************************************************************************
      *  **  OPENLDAP PW POLICY ATTRIBUTES AND CONSTANTS
      *  ************************************************************************
      */
    private static final String OLPW_POLICY_EXTENSION = "2.5.4.35";
    private static final String OLPW_POLICY_CLASS = "pwdPolicy";
    /**
     * This object class combines OpenLDAP PW Policy schema with the Fortress audit context.
     */
    private static final String OAM_PWPOLICY_OBJ_CLASS[] =
        {
            GlobalIds.TOP, "device", OLPW_POLICY_CLASS, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String OLPW_ATTRIBUTE = "pwdAttribute";
    private static final String OLPW_MIN_AGE = "pwdMinAge";
    private static final String OLPW_MAX_AGE = "pwdMaxAge";
    private static final String OLPW_IN_HISTORY = "pwdInHistory";
    private static final String OLPW_CHECK_QUALITY = "pwdCheckQuality";
    private static final String OLPW_MIN_LENGTH = "pwdMinLength";
    private static final String OLPW_EXPIRE_WARNING = "pwdExpireWarning";
    private static final String OLPW_GRACE_LOGIN_LIMIT = "pwdGraceAuthNLimit";
    private static final String OLPW_LOCKOUT = "pwdLockout";
    private static final String OLPW_LOCKOUT_DURATION = "pwdLockoutDuration";
    private static final String OLPW_MAX_FAILURE = "pwdMaxFailure";
    private static final String OLPW_FAILURE_COUNT_INTERVAL = "pwdFailureCountInterval";
    private static final String OLPW_MUST_CHANGE = "pwdMustChange";
    private static final String OLPW_ALLOW_USER_CHANGE = "pwdAllowUserChange";
    private static final String OLPW_SAFE_MODIFY = "pwdSafeModify";
    private static final String[] PASSWORD_POLICY_ATRS =
        {
            OLPW_MIN_AGE, OLPW_MAX_AGE, OLPW_IN_HISTORY, OLPW_CHECK_QUALITY,
            OLPW_MIN_LENGTH, OLPW_EXPIRE_WARNING, OLPW_GRACE_LOGIN_LIMIT, OLPW_LOCKOUT,
            OLPW_LOCKOUT_DURATION, OLPW_MAX_FAILURE, OLPW_FAILURE_COUNT_INTERVAL,
            OLPW_MUST_CHANGE, OLPW_ALLOW_USER_CHANGE, OLPW_SAFE_MODIFY,
    };

    private static final String[] PASSWORD_POLICY_NAME_ATR =
        {
            GlobalIds.CN
    };


    /**
     * @param entity
     * @return
     * @throws org.openldap.fortress.CreateException
     *
     */
    public final PwPolicy create( PwPolicy entity )
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, OAM_PWPOLICY_OBJ_CLASS ) );
            attrs.add( createAttribute( GlobalIds.CN, entity.getName() ) );
            attrs.add( createAttribute( OLPW_ATTRIBUTE, OLPW_POLICY_EXTENSION ) );
            if ( entity.getMinAge() != null )
            {
                attrs.add( createAttribute( OLPW_MIN_AGE, entity.getMinAge().toString() ) );
            }
            if ( entity.getMaxAge() != null )
            {
                attrs.add( createAttribute( OLPW_MAX_AGE, entity.getMaxAge().toString() ) );
            }
            if ( entity.getInHistory() != null )
            {
                attrs.add( createAttribute( OLPW_IN_HISTORY, entity.getInHistory().toString() ) );
            }
            if ( entity.getCheckQuality() != null )
            {
                attrs.add( createAttribute( OLPW_CHECK_QUALITY, entity.getCheckQuality().toString() ) );
            }
            if ( entity.getMinLength() != null )
            {
                attrs.add( createAttribute( OLPW_MIN_LENGTH, entity.getMinLength().toString() ) );
            }
            if ( entity.getExpireWarning() != null )
            {
                attrs.add( createAttribute( OLPW_EXPIRE_WARNING, entity.getExpireWarning().toString() ) );
            }
            if ( entity.getGraceLoginLimit() != null )
            {
                attrs.add( createAttribute( OLPW_GRACE_LOGIN_LIMIT, entity.getGraceLoginLimit().toString() ) );
            }
            if ( entity.getLockout() != null )
            {
                /**
                 * For some reason OpenLDAP requires the pwdLockout boolean value to be upper case:
                 */
                attrs.add( createAttribute( OLPW_LOCKOUT, entity.getLockout().toString().toUpperCase() ) );
            }
            if ( entity.getLockoutDuration() != null )
            {
                attrs.add( createAttribute( OLPW_LOCKOUT_DURATION, entity.getLockoutDuration().toString() ) );
            }
            if ( entity.getMaxFailure() != null )
            {
                attrs.add( createAttribute( OLPW_MAX_FAILURE, entity.getMaxFailure().toString() ) );
            }
            if ( entity.getFailureCountInterval() != null )
            {
                attrs.add( createAttribute( OLPW_FAILURE_COUNT_INTERVAL, entity.getFailureCountInterval().toString() ) );
            }
            if ( entity.getMustChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                attrs.add( createAttribute( OLPW_MUST_CHANGE, entity.getMustChange().toString().toUpperCase() ) );
            }
            if ( entity.getAllowUserChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                attrs.add( createAttribute( OLPW_ALLOW_USER_CHANGE, entity.getAllowUserChange().toString()
                    .toUpperCase() ) );
            }
            if ( entity.getSafeModify() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                attrs.add( createAttribute( OLPW_SAFE_MODIFY, entity.getSafeModify().toString().toUpperCase() ) );
            }

            LDAPEntry myEntry = new LDAPEntry( dn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry, entity );
        }
        catch ( LDAPException e )
        {
            String error = "create name [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new CreateException( GlobalErrIds.PSWD_CREATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }


    /**
     * @param entity
     * @throws org.openldap.fortress.UpdateException
     *
     */
    public final void update( PwPolicy entity )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            if ( entity.getMinAge() != null )
            {
                LDAPAttribute minAge = new LDAPAttribute( OLPW_MIN_AGE, entity.getMinAge().toString() );
                mods.add( LDAPModification.REPLACE, minAge );
            }
            if ( entity.getMaxAge() != null )
            {
                LDAPAttribute maxAge = new LDAPAttribute( OLPW_MAX_AGE, entity.getMaxAge().toString() );
                mods.add( LDAPModification.REPLACE, maxAge );
            }
            if ( entity.getInHistory() != null )
            {
                LDAPAttribute inHistory = new LDAPAttribute( OLPW_IN_HISTORY, entity.getInHistory().toString() );
                mods.add( LDAPModification.REPLACE, inHistory );
            }
            if ( entity.getCheckQuality() != null )
            {
                LDAPAttribute checkQuality = new LDAPAttribute( OLPW_CHECK_QUALITY, entity.getCheckQuality().toString() );
                mods.add( LDAPModification.REPLACE, checkQuality );
            }
            if ( entity.getMinLength() != null )
            {
                LDAPAttribute minLength = new LDAPAttribute( OLPW_MIN_LENGTH, entity.getMinLength().toString() );
                mods.add( LDAPModification.REPLACE, minLength );
            }
            if ( entity.getExpireWarning() != null )
            {
                LDAPAttribute expireWarning = new LDAPAttribute( OLPW_EXPIRE_WARNING, entity.getExpireWarning()
                    .toString() );
                mods.add( LDAPModification.REPLACE, expireWarning );
            }
            if ( entity.getGraceLoginLimit() != null )
            {
                LDAPAttribute graceLoginLimit = new LDAPAttribute( OLPW_GRACE_LOGIN_LIMIT, entity.getGraceLoginLimit()
                    .toString() );
                mods.add( LDAPModification.REPLACE, graceLoginLimit );
            }
            if ( entity.getLockout() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                LDAPAttribute lockout = new LDAPAttribute( OLPW_LOCKOUT, entity.getLockout().toString().toUpperCase() );
                mods.add( LDAPModification.REPLACE, lockout );
            }
            if ( entity.getLockoutDuration() != null )
            {
                LDAPAttribute lockoutDuration = new LDAPAttribute( OLPW_LOCKOUT_DURATION, entity.getLockoutDuration()
                    .toString() );
                mods.add( LDAPModification.REPLACE, lockoutDuration );
            }
            if ( entity.getMaxFailure() != null )
            {
                LDAPAttribute maxFailure = new LDAPAttribute( OLPW_MAX_FAILURE, entity.getMaxFailure().toString() );
                mods.add( LDAPModification.REPLACE, maxFailure );
            }
            if ( entity.getFailureCountInterval() != null )
            {
                LDAPAttribute failureCountInterval = new LDAPAttribute( OLPW_FAILURE_COUNT_INTERVAL, entity
                    .getFailureCountInterval().toString() );
                mods.add( LDAPModification.REPLACE, failureCountInterval );
            }
            if ( entity.getMustChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                LDAPAttribute mustChange = new LDAPAttribute( OLPW_MUST_CHANGE, entity.getMustChange().toString()
                    .toUpperCase() );
                mods.add( LDAPModification.REPLACE, mustChange );
            }
            if ( entity.getAllowUserChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                LDAPAttribute allowUserChange = new LDAPAttribute( OLPW_ALLOW_USER_CHANGE, entity.getAllowUserChange()
                    .toString().toUpperCase() );
                mods.add( LDAPModification.REPLACE, allowUserChange );
            }
            if ( entity.getSafeModify() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                LDAPAttribute safeModify = new LDAPAttribute( OLPW_SAFE_MODIFY, entity.getSafeModify().toString()
                    .toUpperCase() );
                mods.add( LDAPModification.REPLACE, safeModify );
            }
            if ( mods != null && mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LDAPException e )
        {
            String error = "update name [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.PSWD_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param entity
     * @throws org.openldap.fortress.RemoveException
     */
    public final void remove( PwPolicy entity )
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            ld = getAdminConnection();
            delete( ld, dn, entity );
        }
        catch ( LDAPException e )
        {
            String error = "remove name [" + entity.getName() + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.PSWD_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param policy
     * @return
     * @throws org.openldap.fortress.FinderException
     *
     */
    public final PwPolicy getPolicy( PwPolicy policy )
        throws FinderException
    {
        PwPolicy entity = null;
        LDAPConnection ld = null;
        String dn = getDn( policy );
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, dn, PASSWORD_POLICY_ATRS );
            entity = unloadLdapEntry( findEntry, 0 );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
                {
                    String warning = "getPolicy Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
                    throw new FinderException( GlobalErrIds.PSWD_NOT_FOUND, warning );
                }
            }
            else
            {
                String error = "getPolicy name [" + policy.getName() + "] caught LDAPException="
                    + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new FinderException( GlobalErrIds.PSWD_READ_FAILED, error, e );
            }
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }


    /**
     *
     * @param le
     * @param sequence
     * @return
     * @throws LDAPException
     */
    private PwPolicy unloadLdapEntry( LDAPEntry le, long sequence )
    {
        PwPolicy entity = new ObjectFactory().createPswdPolicy();
        entity.setSequenceId( sequence );
        entity.setName( getRdn( le.getDN() ) );
        //entity.setAttribute(getAttribute(le, OLPW_ATTRIBUTE));
        String val = getAttribute( le, OLPW_MIN_AGE );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMinAge( new Integer( val ) );
        }
        val = getAttribute( le, OLPW_MAX_AGE );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMaxAge( new Long( val ) );
        }

        val = getAttribute( le, OLPW_IN_HISTORY );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setInHistory( new Short( val ) );
        }

        val = getAttribute( le, OLPW_CHECK_QUALITY );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setCheckQuality( new Short( val ) );
        }

        val = getAttribute( le, OLPW_MIN_LENGTH );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMinLength( new Short( val ) );
        }

        val = getAttribute( le, OLPW_EXPIRE_WARNING );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setExpireWarning( new Long( val ) );
        }

        val = getAttribute( le, OLPW_GRACE_LOGIN_LIMIT );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setGraceLoginLimit( new Short( val ) );
        }

        val = getAttribute( le, OLPW_LOCKOUT );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setLockout( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, OLPW_LOCKOUT_DURATION );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setLockoutDuration( new Integer( val ) );
        }

        val = getAttribute( le, OLPW_MAX_FAILURE );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMaxFailure( new Short( val ) );
        }

        val = getAttribute( le, OLPW_FAILURE_COUNT_INTERVAL );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setFailureCountInterval( new Short( val ) );
        }

        val = getAttribute( le, OLPW_MUST_CHANGE );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            //noinspection BooleanConstructorCall
            entity.setMustChange( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, OLPW_ALLOW_USER_CHANGE );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setAllowUserChange( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, OLPW_SAFE_MODIFY );
        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setSafeModify( Boolean.valueOf( val ) );
        }
        return entity;
    }


    /**
     * @param policy
     * @return
     * @throws org.openldap.fortress.FinderException
     *
     */
    public final List<PwPolicy> findPolicy( PwPolicy policy )
        throws FinderException
    {
        List<PwPolicy> policyArrayList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String policyRoot = getPolicyRoot( policy.getContextId() );
        String searchVal = null;
        try
        {
            searchVal = encodeSafeText( policy.getName(), GlobalIds.PWPOLICY_NAME_LEN );
            String filter = GlobalIds.FILTER_PREFIX + OLPW_POLICY_CLASS + ")("
                + GlobalIds.POLICY_NODE_TYPE + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, policyRoot,
                LDAPConnection.SCOPE_ONE, filter, PASSWORD_POLICY_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                policyArrayList.add( unloadLdapEntry( searchResults.next(), sequence++ ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "findPolicy name [" + searchVal + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PSWD_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return policyArrayList;
    }


    /**
     * @return
     * @throws FinderException
     */
    public final Set<String> getPolicies( String contextId )
        throws FinderException
    {
        Set<String> policySet = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String policyRoot = getPolicyRoot( contextId );
        try
        {
            String filter = "(objectclass=" + OLPW_POLICY_CLASS + ")";
            ld = getAdminConnection();
            searchResults = search( ld, policyRoot,
                LDAPConnection.SCOPE_ONE, filter, PASSWORD_POLICY_NAME_ATR, false, GlobalIds.BATCH_SIZE );
            while ( searchResults.hasMoreElements() )
            {
                policySet.add( getAttribute( searchResults.next(), GlobalIds.CN ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "getPolicies caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PSWD_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return policySet;
    }


    private String getDn( PwPolicy policy )
    {
        return GlobalIds.POLICY_NODE_TYPE + "=" + policy.getName() + "," + getPolicyRoot( policy.getContextId() );
    }


    private String getPolicyRoot( String contextId )
    {
        return getRootDn( contextId, GlobalIds.PPOLICY_ROOT );
    }
}
