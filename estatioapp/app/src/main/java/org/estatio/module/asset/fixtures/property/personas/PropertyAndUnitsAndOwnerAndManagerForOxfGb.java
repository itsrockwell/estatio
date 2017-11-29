/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package org.estatio.module.asset.fixtures.property.personas;

import org.estatio.module.asset.fixtures.PropertyAndUnitsAndOwnerAndManagerAbstract;
import org.estatio.module.asset.fixtures.property.enums.PropertyAndUnitsAndOwnerAndManager_enum;

public class PropertyAndUnitsAndOwnerAndManagerForOxfGb extends PropertyAndUnitsAndOwnerAndManagerAbstract {

    public static final PropertyAndUnitsAndOwnerAndManager_enum data = PropertyAndUnitsAndOwnerAndManager_enum.OxfGb;

    public static final String REF = data.getRef();
    public static final String PARTY_REF_OWNER = data.getOwner_d().getRef();
    public static final String PARTY_REF_MANAGER = data.getManager_d().getRef();

    public PropertyAndUnitsAndOwnerAndManagerForOxfGb() {
        super(data);
    }

    public static String unitReference(String suffix) {
        return data.getRef() + "-" + suffix;
    }
}
