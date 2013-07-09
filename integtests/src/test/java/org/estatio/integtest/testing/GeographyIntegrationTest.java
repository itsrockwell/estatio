/*
 *
 *  Copyright 2012-2013 Eurocommercial Properties NV
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
package org.estatio.integtest.testing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import org.estatio.dom.geography.Country;
import org.estatio.dom.geography.State;
import org.estatio.fixture.EstatioTransactionalObjectsFixture;
import org.estatio.integtest.AbstractEstatioIntegrationTest;

public class GeographyIntegrationTest extends AbstractEstatioIntegrationTest {

    @Test
    public void countryIsNL() throws Exception {
        assertThat(app.countries.findCountryByReference("NLD").getReference(), is("NLD"));
    }

    @Test
    public void stateCanBeFound() throws Exception {
        final Country country = app.countries.findCountryByReference("NLD");
        final List<State> statesInCountry = app.states.findStatesByCountry(country);
        assertThat(statesInCountry.size(), Matchers.greaterThanOrEqualTo(1));
        for (State state : statesInCountry) {
            assertThat(state.getCountry(), is(country));
        }
    }
}
