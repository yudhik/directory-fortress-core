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
package org.openldap.fortress;

/**
 * This exception is declared to be thrown by all APIs within Fortress Manager interfaces: ({@link org.openldap.fortress.AdminMgr}, {@link org.openldap.fortress.AccessMgr},
 * {@link org.openldap.fortress.ReviewMgr},{@link PwPolicyMgr},{@link org.openldap.fortress.AuditMgr},{@link DelAdminMgr},
 * {@link DelAccessMgr},{@link DelReviewMgr},{@link org.openldap.fortress.cfg.ConfigMgr}).
 * <h3>
 * <p/>The original exception thrown may be of this type or one of its extensions
 * </h3>
 * <ul>
 * <li>{@link AuthorizationException} in the event user fails administrative permission check..
 * <li>{@link CfgException} in the event the runtime cfg system fails.
 * <li>{@link CreateException} in the event DAO cannot create entity.
 * <li>{@link FinderException} in the event DAO cannot find the entity.
 * <li>{@link PasswordException} in the event user fails password checks or password policy exception occurs.
 * <li>{@link RemoveException} in the event DAO cannot remove entity.
 * <li>{@link RestException} during HTTP event failure.
 * <li>{@link UpdateException} in the event DAO cannot update entity.
 * <li>{@link ValidationException} in the event entity validation fails.
 * </ul>
 * <p/>
 * For certain APIs, i.e., {@link org.openldap.fortress.AccessMgr#createSession(org.openldap.fortress.rbac.User, boolean)}, or {@link org.openldap.fortress.AccessMgr#authenticate(String, char[])}, the caller may need to differentiate by one of the above subclasses, e.g. {@link PasswordException}, to facilitate password expiring condition or allow user to retry authentication after entering it incorrectly.
 * If specific exception processing is not the aim, or if differentiating conditions by {@link #errorId} is acceptable, callers are allowed to catch (or throw) as type {@link SecurityException}.
 * <p/>
 * All exceptions generated by Fortress will be set with error code {@link org.openldap.fortress.SecurityException#getErrorId()} indicating fault condition which is set via its constructor - ({@link #SecurityException(int, String)}, {@link #SecurityException(int, String, Exception)}).
 * The error codes are declared in {@link GlobalErrIds} and are also listed below.
 * <h3>
 * <p/>100's - Configuration Errors
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#CONTEXT_NULL} = 101;</code>
 * <li> <code>{@code CONTEXT_SERIALIZATION_FAILED} = 102;</code>*
 * <li> <code>{@link GlobalErrIds#FT_MGR_CLASS_NOT_FOUND} = 103;</code>
 * <li> <code>{@link GlobalErrIds#FT_MGR_INST_EXCEPTION} = 104;</code>
 * <li> <code>{@link GlobalErrIds#FT_MGR_ILLEGAL_ACCESS} = 105;</code>
 * <li> <code>{@link GlobalErrIds#FT_MGR_CLASS_NAME_NULL} = 106;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_NOT_FOUND} = 107;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_NAME_NULL} = 108;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_NAME_INVLD} = 109;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_PROPS_NULL} = 110;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_CREATE_FAILED} = 120;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_UPDATE_FAILED} = 121;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_DELETE_FAILED} = 122;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_DELETE_PROPS_FAILED} = 123;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_READ_FAILED} = 124;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_ALREADY_EXISTS} = 125;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_BOOTSTRAP_FAILED} = 126;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_INITIALIZE_FAILED} = 127;</code>
 * <li> <code>{@link GlobalErrIds#FT_RESOURCE_NOT_FOUND} = 128;</code>
 * <li> <code>{@link GlobalErrIds#FT_CACHE_NOT_CONFIGURED} = 129;</code>
 * <li> <code>{@link GlobalErrIds#FT_CACHE_GET_ERR} = 130;</code>
 * <li> <code>{@link GlobalErrIds#FT_CACHE_PUT_ERR} = 131;</code>
 * <li> <code>{@link GlobalErrIds#FT_CACHE_CLEAR_ERR} = 132;</code>
 * <li> <code>{@link GlobalErrIds#FT_CACHE_FLUSH_ERR} = 133;</code>
 * <li> <code>{@link GlobalErrIds#FT_NULL_CACHE} = 134;</code>
 * <li> <code>{@link GlobalErrIds#FT_APACHE_LDAP_POOL_INIT_FAILED} = 135;</code>
 * <li> <code>{@link GlobalErrIds#FT_CONFIG_JSSE_TRUSTSTORE_NULL} = 136;</code>
 *
 * </ul>
 * <h3>
 * <p/>1000's - User Entity Rule and LDAP Errors
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#USER_SEARCH_FAILED} = 1000;</code>
 * <li> <code>{@link GlobalErrIds#USER_READ_FAILED} = 1001;</code>
 * <li> <code>{@link GlobalErrIds#USER_ADD_FAILED} = 1002;</code>
 * <li> <code>{@link GlobalErrIds#USER_UPDATE_FAILED} = 1003;</code>
 * <li> <code>{@link GlobalErrIds#USER_DELETE_FAILED} = 1004;</code>
 * <li> <code>{@link GlobalErrIds#USER_NOT_FOUND} = 1005;</code>
 * <li> <code>{@link GlobalErrIds#USER_ID_NULL} = 1006;</code>
 * <li> <code>{@link GlobalErrIds#USER_ID_DUPLICATE} = 1007;</code>
 * <li> <code>{@link GlobalErrIds#USER_NULL} = 1008;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_NULL} = 1009;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_INVLD_LEN} = 1010;</code>
 * <li> <code>{@link GlobalErrIds#USER_PLCY_VIOLATION} = 1011;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_PLCY_DEL_FAILED} = 1012;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_INVLD} = 1013;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_CHK_FAILED} = 1014;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_RESET} = 1015;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_LOCKED} = 1016;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_EXPIRED} = 1017;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_MOD_NOT_ALLOWED} = 1018;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_MUST_SUPPLY_OLD} = 1019;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_NSF_QUALITY} = 1020;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_TOO_SHORT} = 1021;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_TOO_YOUNG} = 1022;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_IN_HISTORY} = 1023;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_UNLOCK_FAILED} = 1024;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_LOCK_FAILED} = 1025;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_CHANGE_FAILED} = 1026;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_RESET_FAILED} = 1027;</code>
 * <li> <code>{@link GlobalErrIds#USER_LOCKED_BY_CONST} = 1028;</code>
 * <li> <code>{@link GlobalErrIds#USER_SESS_CREATE_FAILED} = 1029;</code>
 * <li> <code>{@link GlobalErrIds#USER_SESS_NULL} = 1030;</code>
 * <li> <code>{@link GlobalErrIds#USER_ADMIN_NOT_AUTHORIZED} = 1031;</code>
 * <li> <code>{@link GlobalErrIds#USER_CN_NULL} = 1032;</code>
 * <li> <code>{@link GlobalErrIds#USER_SN_NULL} = 1033;</code>
 * <li> <code>{@link GlobalErrIds#USER_PW_PLCY_INVALID} = 1034;</code>
 * <li> <code>{@link GlobalErrIds#USER_OU_INVALID} = 1035;</code>
 * <li> <code>{@link GlobalErrIds#SESS_CTXT_NULL} = 1036;</code>
 * <li> <code>{@link GlobalErrIds#USER_BIND_FAILED} = 1037;</code>
 *
 *
 * </ul>
 * <h3>
 * <p/>2000's User-Role assignments
 * </h3>
 * <h4>
 * <p/> User-Role Rule and LDAP errors
 * </h4>
 * <ul>
 * <li> <code>{@link GlobalErrIds#URLE_NULL} = 2003;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ASSIGN_FAILED} = 2004;</code>
 * <li> <code>{@link GlobalErrIds#URLE_DEASSIGN_FAILED} = 2005;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ACTIVATE_FAILED} = 2006;</code>
 * <li> <code>{@link GlobalErrIds#URLE_DEACTIVE_FAILED} = 2007;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ASSIGN_EXIST} = 2008;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ASSIGN_NOT_EXIST} = 2009;</code>
 * <li> <code>{@link GlobalErrIds#URLE_SEARCH_FAILED} = 2010;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ALREADY_ACTIVE} = 2011;</code>
 * <li> <code>{@link GlobalErrIds#URLE_NOT_ACTIVE} = 2022;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ADMIN_CANNOT_ASSIGN} = 2023;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ADMIN_CANNOT_DEASSIGN} = 2024;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ADMIN_CANNOT_GRANT} = 2025;</code>
 * <li> <code>{@link GlobalErrIds#URLE_ADMIN_CANNOT_REVOKE} = 2026;</code>
 * </ul>
 * <h4>
 * <p/> Temporal Constraint Activation Violations
 * </h4>
 * <ul>
 * <li> <code>{@link GlobalErrIds#ACTV_FAILED_DAY} = 2050;</code>
 * <li> <code>{@link GlobalErrIds#ACTV_FAILED_DATE} = 2051;</code>
 * <li> <code>{@link GlobalErrIds#ACTV_FAILED_TIME} = 2052;</code>
 * <li> <code>{@link GlobalErrIds#ACTV_FAILED_TIMEOUT} = 2053;</code>
 * <li> <code>{@link GlobalErrIds#ACTV_FAILED_LOCK} = 2054;</code>
 * <li> <code>{@link GlobalErrIds#ACTV_FAILED_DSD} = 2055;</code>
 * </ul>
 * <h3>
 * <p/>3000's - Permission Entity
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#PERM_SEARCH_FAILED} = 3000;</code>
 * <li> <code>{@link GlobalErrIds#PERM_READ_OP_FAILED} = 3001;</code>
 * <li> <code>{@link GlobalErrIds#PERM_READ_OBJ_FAILED} = 3002;</code>
 * <li> <code>{@link GlobalErrIds#PERM_ADD_FAILED} = 3003;</code>
 * <li> <code>{@link GlobalErrIds#PERM_UPDATE_FAILED} = 3004;</code>
 * <li> <code>{@link GlobalErrIds#PERM_DELETE_FAILED} = 3005;</code>
 * <li> <code>{@link GlobalErrIds#PERM_OP_NOT_FOUND} = 3006;</code>
 * <li> <code>{@link GlobalErrIds#PERM_OBJ_NOT_FOUND} = 3007;</code>
 * <li> <code>{@link GlobalErrIds#PERM_NULL} = 3008;</code>
 * <li> <code>{@link GlobalErrIds#PERM_OPERATION_NULL} = 3009;</code>
 * <li> <code>{@link GlobalErrIds#PERM_OBJECT_NULL} = 3010;</code>
 * <li> <code>{@link GlobalErrIds#PERM_DUPLICATE} = 3011;</code>
 * <li> <code>{@link GlobalErrIds#PERM_GRANT_FAILED} = 3012;</code>
 * <li> <code>{@link GlobalErrIds#PERM_GRANT_USER_FAILED} = 3013;</code>
 * <li> <code>{@link GlobalErrIds#PERM_REVOKE_FAILED} = 3024;</code>
 * <li> <code>{@link GlobalErrIds#PERM_ROLE_EXIST} = 3015;</code>
 * <li> <code>{@link GlobalErrIds#PERM_ROLE_NOT_EXIST} = 3016;</code>
 * <li> <code>{@link GlobalErrIds#PERM_USER_EXIST} = 3017;</code>
 * <li> <code>{@link GlobalErrIds#PERM_USER_NOT_EXIST} = 3018;</code>
 * <li> <code>{@link GlobalErrIds#PERM_ROLE_SEARCH_FAILED} = 3019;</code>
 * <li> <code>{@link GlobalErrIds#PERM_USER_SEARCH_FAILED} = 3020;</code>
 * <li> <code>{@link GlobalErrIds#PERM_SESS_SEARCH_FAILED} = 3021;</code>
 * <li> <code>{@link GlobalErrIds#PERM_BULK_USER_REVOKE_FAILED} = 3022;</code>
 * <li> <code>{@link GlobalErrIds#PERM_BULK_ROLE_REVOKE_FAILED} = 3023;</code>
 * <li> <code>{@link GlobalErrIds#PERM_BULK_ADMINROLE_REVOKE_FAILED} = 3024;</code>
 * <li> <code>{@link GlobalErrIds#PERM_OU_INVALID} = 3025;</code>
 * <li> <code>{@link GlobalErrIds#PERM_OPERATION_NM_NULL} = 3026;</code>
 * <li> <code>{@link GlobalErrIds#PERM_OBJECT_NM_NULL} = 3027;</code>
 * <li> <code>{@link GlobalErrIds#PERM_COMPARE_OP_FAILED} = 3028;</code>
 * <li> <code>{@link GlobalErrIds#PERM_NOT_EXIST} = 3029;</code>
 * </ul>
 * <h3>
 * <p/>4000's - Password Policy Entity
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#PSWD_READ_FAILED} = 4000;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_CREATE_FAILED} = 4001;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_UPDATE_FAILED} = 4002;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_DELETE_FAILED} = 4003;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_SEARCH_FAILED} = 4004;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_NOT_FOUND} = 4005;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_NAME_INVLD_LEN} = 4006;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_QLTY_INVLD_LEN} = 4007;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_QLTY_INVLD} = 4008;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_MAXAGE_INVLD} = 4009;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_MINAGE_INVLD} = 4010;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_MINLEN_INVLD} = 4011;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_INTERVAL_INVLD} = 4012;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_MAXFAIL_INVLD} = 4013;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_MUSTCHG_INVLD} = 4014;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_SAFECHG_INVLD} = 4015;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_ALLOWCHG_INVLD} = 4016;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_HISTORY_INVLD} = 4017;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_GRACE_INVLD} = 4018;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_LOCKOUTDUR_INVLD} = 4019;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_EXPWARN_INVLD} = 4020;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_LOCKOUT_INVLD} = 4021;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_NAME_NULL} = 4022;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_PLCY_NULL} = 4023;</code>
 * <li> <code>{@link GlobalErrIds#PSWD_CONST_VIOLATION} = 4024;</code>
 * </ul>
 * <h3>
 * <p/>5000's - RBAC
 * </h3>
 * <h4>
 * <p/> Role Rule and System errors
 * </h4>
 * <ul>
 * <li> <code>{@link GlobalErrIds#ROLE_SEARCH_FAILED} = 5000;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_READ_FAILED} = 5001;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_ADD_FAILED} = 5002;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_UPDATE_FAILED} = 5003;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_DELETE_FAILED} = 5004;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_NM_NULL} = 5005;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_NOT_FOUND} = 5006;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_NULL} = 5007;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_USER_ASSIGN_FAILED} = 5008;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_USER_DEASSIGN_FAILED} = 5009;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_LST_NULL} = 5010;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_OCCUPANT_SEARCH_FAILED} = 5011;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_REMOVE_OCCUPANT_FAILED} = 5012;</code>
 * <li> <code>{@link GlobalErrIds#PARENT_ROLE_NULL} = 5013;</code>
 * <li> <code>{@link GlobalErrIds#CHILD_ROLE_NULL} = 5014;</code>
 * <li> <code>{@link GlobalErrIds#ROLE_REMOVE_PARENT_FAILED} = 5015;</code>
 * </ul>
 * <h4>
 * <p/> Hierarchical Constraints
 * </h4>
 * <ul>
 * <li> <code>{@link GlobalErrIds#HIER_READ_FAILED} = 5051;</code>
 * <li> <code>{@link GlobalErrIds#HIER_ADD_FAILED} = 5052;</code>
 * <li> <code>{@link GlobalErrIds#HIER_UPDATE_FAILED} = 5053;</code>
 * <li> <code>{@link GlobalErrIds#HIER_DELETE_FAILED} = 5054;</code>
 * <li> <code>{@link GlobalErrIds#HIER_NOT_FOUND} = 5056;</code>
 * <li> <code>{@link GlobalErrIds#HIER_REL_INVLD} = 5057;</code>
 * <li> <code>{@link GlobalErrIds#HIER_DEL_FAILED_HAS_CHILD} = 5058;</code>
 * <li> <code>{@link GlobalErrIds#HIER_REL_EXIST} = 5059;</code>
 * <li> <code>{@link GlobalErrIds#HIER_REL_NOT_EXIST} = 5060;</code>
 * <li> <code>{@link GlobalErrIds#HIER_NULL} = 0561;</code>
 * <li> <code>{@link GlobalErrIds#HIER_TYPE_NULL} = 5062;</code>
 * <li> <code>{@link GlobalErrIds#HIER_CANNOT_PERFORM} = 5063;</code>
 * <li> <code>{@link GlobalErrIds#HIER_REL_CYCLIC} = 5064;</code>
 *
 * </ul>
 * <h4>
 * <p/> Separation of Duty Relations
 * </h4>
 * <ul>
 * <li> <code>{@link GlobalErrIds#SSD_SEARCH_FAILED} = 5080;</code>
 * <li> <code>{@link GlobalErrIds#SSD_READ_FAILED} = 5081;</code>
 * <li> <code>{@link GlobalErrIds#SSD_ADD_FAILED} = 5082;</code>
 * <li> <code>{@link GlobalErrIds#SSD_UPDATE_FAILED} = 5083;</code>
 * <li> <code>{@link GlobalErrIds#SSD_DELETE_FAILED} = 0584;</code>
 * <li> <code>{@link GlobalErrIds#SSD_NM_NULL} = 5085;</code>
 * <li> <code>{@link GlobalErrIds#SSD_NOT_FOUND} = 5086;</code>
 * <li> <code>{@link GlobalErrIds#SSD_NULL} = 5087;</code>
 * <li> <code>{@link GlobalErrIds#SSD_VALIDATION_FAILED} = 5088;</code>
 * <li> <code>{@link GlobalErrIds#DSD_SEARCH_FAILED} = 5089;</code>
 * <li> <code>{@link GlobalErrIds#DSD_READ_FAILED} = 5090;</code>
 * <li> <code>{@link GlobalErrIds#DSD_ADD_FAILED} = 5091;</code>
 * <li> <code>{@link GlobalErrIds#DSD_UPDATE_FAILED} = 5092;</code>
 * <li> <code>{@link GlobalErrIds#DSD_DELETE_FAILED} = 5093;</code>
 * <li> <code>{@link GlobalErrIds#DSD_NM_NULL} = 5094;</code>
 * <li> <code>{@link GlobalErrIds#DSD_NOT_FOUND} = 5095;</code>
 * <li> <code>{@link GlobalErrIds#DSD_NULL} = 5096;</code>
 * <li> <code>{@link GlobalErrIds#DSD_VALIDATION_FAILED} = 5097;</code>
 * </ul>
 * <h3>
 * <p/>6000's - LDAP Suffix and Container Entities
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#CNTR_CREATE_FAILED} = 6001;</code>
 * <li> <code>{@link GlobalErrIds#CNTR_DELETE_FAILED} = 6002;</code>
 * <li> <code>{@link GlobalErrIds#CNTR_NAME_NULL} = 6003;</code>
 * <li> <code>{@link GlobalErrIds#CNTR_NAME_INVLD} = 6004;</code>
 * <li> <code>{@link GlobalErrIds#CNTR_PARENT_NULL} = 6005;</code>
 * <li> <code>{@link GlobalErrIds#CNTR_PARENT_INVLD} = 6006;</code>
 * <li> <code>{@link GlobalErrIds#SUFX_CREATE_FAILED} = 6010;</code>
 * <li> <code>{@link GlobalErrIds#SUFX_DELETE_FAILED} = 6011;</code>
 * <li> <code>{@link GlobalErrIds#SUFX_NAME_NULL} = 6012;</code>
 * <li> <code>{@link GlobalErrIds#SUFX_NAME_INVLD} = 6013;</code>
 * <li> <code>{@link GlobalErrIds#SUFX_DCTOP_NULL} = 6014;</code>
 * <li> <code>{@link GlobalErrIds#SUFX_DCTOP_INVLD} = 6015;</code>
 * </ul>
 * <h3>
 * <p/>7000's - Audit Activities
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#AUDT_BIND_SEARCH_FAILED} = 7000;</code>
 * <li> <code>{@link GlobalErrIds#AUDT_INPUT_NULL} = 7001;</code>
 * <li> <code>{@link GlobalErrIds#AUDT_AUTHZ_SEARCH_FAILED} = 7002;</code>
 * <li> <code>{@link GlobalErrIds#AUDT_MOD_SEARCH_FAILED} = 7003;</code>
 * <li> <code>{@link GlobalErrIds#AUDT_MOD_ADMIN_SEARCH_FAILED} = 7004;</code>
 * <li> <code>{@link GlobalErrIds#AUDT_AUTHN_INVALID_FAILED} = 7005;</code>
 * </ul>
 * <h3>
 * <p/>8000's Organizational Unit Rule and System errors
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#ORG_NULL} = 8001;</code>
 * <li> <code>{@link GlobalErrIds#ORG_TYPE_NULL} = 8002;</code>
 * <li> <code>{@link GlobalErrIds#ORG_READ_FAILED_USER} = 8011;</code>
 * <li> <code>{@link GlobalErrIds#ORG_ADD_FAILED_USER} = 8012;</code>
 * <li> <code>{@link GlobalErrIds#ORG_UPDATE_FAILED_USER} = 8013;</code>
 * <li> <code>{@link GlobalErrIds#ORG_DELETE_FAILED_USER} = 8014;</code>
 * <li> <code>{@link GlobalErrIds#ORG_SEARCH_FAILED_USER} = 8015;</code>
 * <li> <code>{@link GlobalErrIds#ORG_GET_FAILED_USER} = 0816;</code>
 * <li> <code>{@link GlobalErrIds#ORG_NOT_FOUND_USER} = 8017;</code>
 * <li> <code>{@link GlobalErrIds#ORG_NULL_USER} = 0818;</code>
 * <li> <code>{@link GlobalErrIds#ORG_TYPE_NULL_USER} = 8019;</code>
 * <li> <code>{@link GlobalErrIds#ORG_DEL_FAILED_USER} = 8020;</code>
 * <li> <code>{@link GlobalErrIds#ORG_REMOVE_PARENT_FAILED_USER} = 8021;</code>
 * <li> <code>{@link GlobalErrIds#ORG_READ_FAILED_PERM} = 8061;</code>
 * <li> <code>{@link GlobalErrIds#ORG_ADD_FAILED_PERM} = 8062;</code>
 * <li> <code>{@link GlobalErrIds#ORG_UPDATE_FAILED_PERM} = 8063;</code>
 * <li> <code>{@link GlobalErrIds#ORG_DELETE_FAILED_PERM} = 8064;</code>
 * <li> <code>{@link GlobalErrIds#ORG_SEARCH_FAILED_PERM} = 8065;</code>
 * <li> <code>{@link GlobalErrIds#ORG_GET_FAILED_PERM} = 8066;</code>
 * <li> <code>{@link GlobalErrIds#ORG_NOT_FOUND_PERM} = 8067;</code>
 * <li> <code>{@link GlobalErrIds#ORG_NULL_PERM} = 8068;</code>
 * <li> <code>{@link GlobalErrIds#ORG_TYPE_NULL_PERM} = 8069;</code>
 * <li> <code>{@link GlobalErrIds#ORG_DEL_FAILED_PERM} = 8070;</code>
 * <li> <code>{@link GlobalErrIds#ORG_LEN_INVLD} = 8071;</code>
 * <li> <code>{@link GlobalErrIds#ORG_PARENT_NULL} = 8072;</code>
 * <li> <code>{@link GlobalErrIds#ORG_CHILD_NULL} = 8073;</code>
 * <li> <code>{@link GlobalErrIds#ORG_REMOVE_PARENT_FAILED_PERM} = 8074;</code>
 * </ul>
 * <h3>
 * <p/>9000's Administrative RBAC
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#ARLE_SEARCH_FAILED} = 9000;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_READ_FAILED} = 9001;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_ADD_FAILED} = 9002;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_UPDATE_FAILED} = 9003;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_DELETE_FAILED} = 9004;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_NM_NULL} = 9005;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_NOT_FOUND} = 9006;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_NULL} = 9007;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_USER_ASSIGN_FAILED} = 9008;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_USER_DEASSIGN_FAILED} = 9009;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_LST_NULL} = 9010;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_BEGIN_RANGE_NULL} = 9011;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_END_RANGE_NULL} = 0911;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_INVLD_RANGE} = 9012;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_INVLD_RANGE_INCLUSIVE} = 9013;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_ACTIVATE_FAILED} = 9014;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_DEACTIVE_FAILED} = 9015;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_ALREADY_ACTIVE} = 9016;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_NOT_ACTIVE} = 9017;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_USER_SEARCH_FAILED} = 9018;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_PARENT_NULL} = 9019;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_CHILD_NULL} = 9020;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_ASSIGN_EXIST} = 9021;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_ASSIGN_NOT_EXIST} = 9022;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_DEASSIGN_NOT_EXIST} = 9023;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_ASSIGN_FAILED} = 9024;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_DEASSIGN_FAILED} = 9025;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_OCCUPANT_SEARCH_FAILED} = 9026;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_REMOVE_OCCUPANT_FAILED} = 9027;</code>
 * <li> <code>{@link GlobalErrIds#ARLE_REMOVE_PARENT_FAILED} = 9028;</code>
 * </ul>
 *
 * <h3>
 * <p/>10000's - Temporal Constraint Validation Error Ids
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#CONST_INVLD_TEXT} = 10001;</code>
 * <li> <code>{@link GlobalErrIds#CONST_INVLD_FIELD_LEN} = 10002;</code>
 * <li> <code>{@link GlobalErrIds#CONST_TIMEOUT_INVLD} = 10003;</code>
 * <li> <code>{@link GlobalErrIds#CONST_BEGINTIME_INVLD} = 10004;</code>
 * <li> <code>{@link GlobalErrIds#CONST_BEGINTIME_LEN_ERR} = 10005;</code>
 * <li> <code>{@link GlobalErrIds#CONST_ENDTIME_INVLD} = 10006;</code>
 * <li> <code>{@link GlobalErrIds#CONST_ENDTIME_LEN_ERR} = 10007;</code>
 * <li> <code>{@link GlobalErrIds#CONST_BEGINDATE_INVLD} = 10008;</code>
 * <li> <code>{@link GlobalErrIds#CONST_BEGINDATE_NULL} = 10009;</code>
 * <li> <code>{@link GlobalErrIds#CONST_ENDDATE_INVLD} = 10010;</code>
 * <li> <code>{@link GlobalErrIds#CONST_ENDDATE_NULL} = 10011;</code>
 * <li> <code>{@link GlobalErrIds#CONST_DAYMASK_INVLD} = 10012;</code>
 * <li> <code>{@link GlobalErrIds#CONST_DAYMASK_NULL} = 10013;</code>
 * <li> <code>{@link GlobalErrIds#CONST_DESC_LEN_INVLD} = 10014;</code>
 * <li> <code>{@link GlobalErrIds#CONST_NULL_TEXT} = 10015;</code>
 * </ul>
 * <h3>
 * <p/>10100's - REST calls through remote En Masse Interface Error Ids
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#REST_WEB_ERR} = 10101;</code>
 * <li> <code>{@link GlobalErrIds#REST_IO_ERR} = 10102;</code>
 * <li> <code>{@link GlobalErrIds#REST_MARSHALL_ERR} = 10103;</code>
 * <li> <code>{@link GlobalErrIds#REST_UNMARSHALL_ERR} = 10104;</code>
 * <li> <code>{@link GlobalErrIds#REST_GET_FAILED} = 10105;</code>
 * <li> <code>{@link GlobalErrIds#REST_NOT_FOUND_ERR} = 10106;</code>
 * <li> <code>{@link GlobalErrIds#REST_UNKNOWN_ERR} = 10107;</code>
 * <li> <code>{@link GlobalErrIds#REST_FORBIDDEN_ERR} = 10108;</code>
 * <li> <code>{@link GlobalErrIds#REST_UNAUTHORIZED_ERR} = 10109;</code>
 * </ul>
 * <h3>
 * <p/>10200's - RBAC Accelerator extended LDAP operation Error Ids
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#ACEL_CREATE_SESSION_ERR} = 10201;</code>
 * <li> <code>{@link GlobalErrIds#ACEL_DELETE_SESSION_ERR} = 10202;</code>
 * <li> <code>{@link GlobalErrIds#ACEL_CHECK_ACCESS_ERR} = 10203;</code>
 * <li> <code>{@link GlobalErrIds#ACEL_ADD_ROLE_ERR} = 10204;</code>
 * <li> <code>{@link GlobalErrIds#ACEL_DROP_ROLE_ERR} = 10205;</code>
 * </ul>
 * <h3>
 * <p/>10300's - LDAP Group operation Error Ids
 * </h3>
 * <ul>
 * <li> <code>{@link GlobalErrIds#GROUP_SEARCH_FAILED} = 10300;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_READ_FAILED} = 10301;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_ADD_FAILED} = 10302;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_UPDATE_FAILED} = 10303;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_DELETE_FAILED} = 10304;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_ADD_PROPERTY_FAILED} = 10305;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_DELETE_PROPERTY_FAILED} = 10306;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_NOT_FOUND} = 10307;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_NULL} = 10308;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_USER_ASSIGN_FAILED} = 10309;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_USER_DEASSIGN_FAILED} = 10310;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_NAME_NULL} = 10311;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_NAME_INVLD} = 10312;</code>
 * <li> <code>{@link GlobalErrIds#GROUP_PROTOCOL_INVLD} = 10313;</code>
 * </ul>
 * <p/>
 *
 * @author Shawn McKinney
 */
public class SecurityException extends BaseException
{
    /**
     * Create exception with error id and message.
     * @param  errorId see {@link GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg contains textual information including method of origin and description of the root cause.
     */
    public SecurityException(int errorId, String msg)
    {
        super(errorId, msg);
    }


    /**
     * Create exception with error id, message and related exception.
     * @param  errorId see {@link GlobalErrIds} for list of valid error codes.
     * @param msg contains textual information including method of origin and description of the root cause.
     * @param previousException contains reference to related exception which usually is system related, i.e. ldap.
     */
    public SecurityException(int errorId, String msg, Exception previousException)
    {
        super(errorId, msg, previousException);
    }
}