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
package org.openldap.fortress.cfg;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.exception.LdapEntryAlreadyExistsException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.openldap.fortress.ldap.ApacheDsDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.FinderException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.RemoveException;
import org.openldap.fortress.UpdateException;
import org.openldap.fortress.util.attr.AttrHelper;
import org.openldap.fortress.util.attr.VUtil;

/**
 * This class provides data access for the standard ldap object device that has been extended to support name/value pairs.
 * Fortress uses this data structure to store its remote cfg parameters.
 * <p/>
 * The Fortress Config node is a combination of:
 * <p/>
 * 'device' Structural Object Class is used to store basic attributes like cn which will be used for config node name.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.14 NAME 'device'</code>
 * <li> <code>DESC 'RFC2256: a device''</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( serialNumber $ seeAlso $ owner $ ou $ o $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 'ftProperties' AUXILIARY Object Class is used to store name/value pairs on target node.<br />
 * <code>This aux object class can be used to store custom attributes.</code><br />
 * <code>The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
final class ConfigDAO extends ApacheDsDataProvider

{
    private static final String CLS_NM = ConfigDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String CONFIG_ROOT_PARAM = "config.root";
    private static final String CONFIG_ROOT_DN = Config.getProperty( CONFIG_ROOT_PARAM );
    private static final String DEVICE_OBJECT_CLASS_NM = "device";

    private static final String CONFIG_OBJ_CLASS[] =
        {
            DEVICE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME
    };

    private static final String[] CONFIG_ATRS =
        {
            GlobalIds.CN, GlobalIds.PROPS
    };

    /**
     * Package private default constructor.
     */
    ConfigDAO()
    {
    }

    /**
     * @param name
     * @param props
     * @return
     * @throws org.openldap.fortress.CreateException
     */
    final Properties create( String name, Properties props )
        throws org.openldap.fortress.CreateException
    {
        LdapConnection ld = null;
        String dn = getDn( name );
        LOG.info( "create dn [" + dn + "]" );
        try
        {
            Entry myEntry = new DefaultEntry( dn );
            myEntry.add( GlobalIds.OBJECT_CLASS, CONFIG_OBJ_CLASS );
            ld = getAdminConnection();
            myEntry.add( GlobalIds.CN, name );
            loadProperties( props, myEntry, GlobalIds.PROPS );
            add( ld, myEntry );
        }
        catch ( LdapEntryAlreadyExistsException e )
        {
            String warning = "create config dn [" + dn + "] caught LdapEntryAlreadyExistsException="
                + e.getMessage() + " msg=" + e.getMessage();
            throw new org.openldap.fortress.CreateException( GlobalErrIds.FT_CONFIG_ALREADY_EXISTS, warning );
        }
        catch ( LdapException e )
        {
            String error;
            error = "create config dn [" + dn + "] caught LDAPException=" + e.getMessage();
            LOG.error( error, e );
            throw new org.openldap.fortress.CreateException( GlobalErrIds.FT_CONFIG_CREATE_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     * @param name
     * @param props
     * @return
     * @throws org.openldap.fortress.UpdateException
     */
    final Properties update( String name, Properties props )
        throws org.openldap.fortress.UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( name );
        LOG.info( "update dn [" + dn + "]" );
        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            if ( VUtil.isNotNullOrEmpty( props ) )
            {
                loadProperties( props, mods, GlobalIds.PROPS, true );
            }
            ld = getAdminConnection();
            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods );
            }
        }
        catch ( LdapException e )
        {
            String error = "update dn [" + dn + "] caught LDAPException=" + e.getMessage();
            throw new org.openldap.fortress.UpdateException( GlobalErrIds.FT_CONFIG_UPDATE_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     * @param name
     * @throws org.openldap.fortress.RemoveException
     */
    final void remove( String name )
        throws RemoveException
    {
        LdapConnection ld = null;
        String dn = getDn( name );
        LOG.info( "remove dn [" + dn + "]" );
        try
        {
            ld = getAdminConnection();
            delete( ld, dn );
        }
        catch ( LdapException e )
        {
            String error = "remove dn [" + dn + "] LDAPException=" + e.getMessage();
            throw new org.openldap.fortress.RemoveException( GlobalErrIds.FT_CONFIG_DELETE_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param name
     * @param props
     * @return
     * @throws org.openldap.fortress.UpdateException
     */
    final Properties remove( String name, Properties props )
        throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( name );
        LOG.info( "remove props dn [" + dn + "]" );
        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            if ( VUtil.isNotNullOrEmpty( props ) )
            {
                removeProperties( props, mods, GlobalIds.PROPS );
            }
            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods );
            }
        }
        catch ( LdapException e )
        {
            String error = "remove props dn [" + dn + "] caught LDAPException=" + e.getMessage();
            throw new org.openldap.fortress.UpdateException( GlobalErrIds.FT_CONFIG_DELETE_PROPS_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     * @param name
     * @return
     * @throws org.openldap.fortress.FinderException
     */
    final Properties getConfig( String name )
        throws FinderException
    {
        Properties props = null;
        LdapConnection ld = null;
        String dn = getDn( name );
        LOG.info( "getConfig dn [" + dn + "]" );
        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, CONFIG_ATRS );
            props = AttrHelper.getProperties( getAttributes( findEntry, GlobalIds.PROPS ) );
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "getConfig COULD NOT FIND ENTRY for dn [" + dn + "]";
            throw new FinderException( GlobalErrIds.USER_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "getConfig dn [" + dn + "] caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.FT_CONFIG_READ_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     *
     * @param name
     * @return
     */
    private String getDn( String name )
    {
        return GlobalIds.CN + "=" + name + "," + CONFIG_ROOT_DN;
    }
}
