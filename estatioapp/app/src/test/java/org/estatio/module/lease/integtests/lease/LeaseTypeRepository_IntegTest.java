package org.estatio.module.lease.integtests.lease;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.estatio.module.lease.dom.Lease;
import org.estatio.module.lease.dom.LeaseRepository;
import org.estatio.module.lease.dom.LeaseType;
import org.estatio.module.lease.dom.LeaseTypeRepository;
import org.estatio.module.lease.fixtures.lease.LeaseForOxfTopModel001Gb;
import org.estatio.module.lease.fixtures.lease.LeaseItemAndTermsForOxfTopModel001;
import org.estatio.module.lease.integtests.LeaseModuleIntegTestAbstract;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LeaseTypeRepository_IntegTest extends LeaseModuleIntegTestAbstract {

    @Before
    public void setupData() {
        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {


                executionContext.executeChild(this, new LeaseItemAndTermsForOxfTopModel001());
            }
        });
    }

    @Inject
    LeaseRepository leaseRepository;

    @Inject
    LeaseTypeRepository leaseTypeRepository;

    Lease lease;

    @Before
    public void setUp() throws Exception {
        lease = leaseRepository.findLeaseByReference(LeaseForOxfTopModel001Gb.REF);
    }

    public static class FindByReference extends LeaseTypeRepository_IntegTest {

        @Test
        public void findByReference() throws Exception {
            LeaseType leaseType = leaseTypeRepository.findByReference(lease.getLeaseType().getReference());
            assertThat(leaseType, is(lease.getLeaseType()));
        }
    }

}