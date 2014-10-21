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
package org.openldap.fortress.util;

import org.slf4j.LoggerFactory;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.util.attr.VUtil;

/**
 * Contains a simple wrapper for log4j that is used by test utilities.
 *
 * @author Shawn McKinney
 */
public class LogUtil
{
    //final private static Logger log = Logger.getLogger(LogUtil.class.getName());
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( LogUtil.class.getName() );


    /**
     * Write a message out to the appropriate log level.
     *
     * @param msg Contains message to write out to log.
     */
    public static void logIt(String msg)
    {
        if(VUtil.isNotNullOrEmpty( getContext() ))
            msg = getContext() + " " + msg;

        if(LOG.isDebugEnabled())
        {
            LOG.debug( msg );
        }
        else if(LOG.isInfoEnabled())
        {
            LOG.info( msg );
        }
        else if(LOG.isWarnEnabled())
        {
            LOG.warn( msg );
        }
        else if(LOG.isErrorEnabled())
        {
            LOG.error( msg );
        }
	}

    public static String getContext()
    {
        String contextId = null;
        String tenant = System.getProperty( GlobalIds.TENANT );
        if ( VUtil.isNotNullOrEmpty( tenant ) && !tenant.equals( "${tenant}" ) )
        {
            contextId = tenant;
        }
        return contextId;
    }
}

