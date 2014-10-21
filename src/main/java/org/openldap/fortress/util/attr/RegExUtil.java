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
package org.openldap.fortress.util.attr;

import org.openldap.fortress.GlobalErrIds;

import org.openldap.fortress.GlobalIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Regular expression utilities to perform data validations on Fortress attributes.  These utils use the standard
 * java regular expression library.
 *
 * @author     Shawn McKinney
 */
class RegExUtil
{
    private static final String CLS_NM = RegExUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
	private static final String safeTextPatternStr = org.openldap.fortress.cfg.Config.getProperty(GlobalIds.REG_EX_SAFE_TEXT);

	/**
	 *  Perform safe text validation on character string.
	 *
	 * @param  value Contains the string to check.
	 * @exception org.openldap.fortress.ValidationException  In the event the data validation fails.
	 */
	public static void safeText(String value)
		throws org.openldap.fortress.ValidationException
	{
		if (safeTextPatternStr == null || safeTextPatternStr.compareTo("") == 0)
		{
			LOG.debug("safeText can't find safeText regular expression pattern.  Check your Fortress cfg");
		}
		else
		{
			Pattern safeTextPattern = Pattern.compile(safeTextPatternStr);
			Matcher safeTextMatcher = safeTextPattern.matcher(value);
			if (!safeTextMatcher.find())
			{
				String error = "safeText has detected invalid value [" + value + "]";
				throw new org.openldap.fortress.ValidationException(GlobalErrIds.CONST_INVLD_TEXT, error);
			}
		}
	}
}