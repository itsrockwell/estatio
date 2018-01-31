/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.command;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;

import org.apache.isis.applib.ModuleAbstract;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.audit.AuditModule;
import org.isisaddons.module.command.dom.CommandJdo;

import org.incode.module.fixturesupport.dom.scripts.TeardownFixtureAbstract;

/**
 * This is a "proxy" for the corresponding module defined in the Incode Platform.
 */
@XmlRootElement(name = "module")
public final class IncodeSpiCommandModule extends ModuleAbstract {

    @Override
    public Set<Class<?>> getAdditionalModules() {
        return Sets.newHashSet(CommandModule.class, AuditModule.class);
    }


    @Override
    public FixtureScript getTeardownFixture() {
        // can't delete from CommandJdo, is searched for during teardown (IsisSession#close)
        return null;
    }

    /**
     * For tests that need to delete the command table first.
     * Should be run in the @Before of the test.
     */
    public FixtureScript getTeardownFixtureWillDelete() {
        return new TeardownFixtureAbstract() {
            @Override
            protected void execute(final ExecutionContext executionContext) {
                deleteFrom(CommandJdo.class);
            }
        };
    }


}
