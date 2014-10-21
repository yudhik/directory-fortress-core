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
package org.openldap.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link org.openldap.fortress.rbac.Role}, {@link org.openldap.fortress.rbac.Permission} and {@link org.openldap.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortRolePerm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rolePerm", propOrder = {
    "role",
    "perm"
})
public class RolePerm extends FortEntity
    implements java.io.Serializable
{
    private Role role;
    private Permission perm;

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public Permission getPerm()
    {
        return perm;
    }

    public void setPerm(Permission perm)
    {
        this.perm = perm;
    }
}