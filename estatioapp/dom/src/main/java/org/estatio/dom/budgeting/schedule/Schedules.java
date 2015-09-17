/*
 *
 *  Copyright 2012-2015 Eurocommercial Properties NV
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
package org.estatio.dom.budgeting.schedule;

import java.util.List;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.dom.UdoDomainRepositoryAndFactory;
import org.estatio.dom.asset.Property;
import org.estatio.dom.budgeting.budget.Budget;
import org.estatio.dom.charge.Charge;
import org.estatio.dom.valuetypes.LocalDateInterval;

@DomainService(repositoryFor = Schedule.class, nature = NatureOfService.DOMAIN)
@DomainServiceLayout()
public class Schedules extends UdoDomainRepositoryAndFactory<Schedule> {

    public Schedules() {
        super(Schedules.class, Schedule.class);
    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public Schedule newSchedule(
            final Property property,
            final Budget budget,
            final LocalDate startDate,
            final LocalDate endDate,
            final Charge charge,
            final Schedule.Status status) {
        Schedule newSchedule = newTransientInstance();
        newSchedule.setProperty(property);
        newSchedule.setBudget(budget);
        newSchedule.setStartDate(startDate);
        newSchedule.setEndDate(endDate);
        newSchedule.setCharge(charge);
        newSchedule.setStatus(status);
        persistIfNotAlready(newSchedule);

        return newSchedule;
    }

    public String validateNewSchedule(
            final Property property,
            final Budget budget,
            final LocalDate startDate,
            final LocalDate endDate,
            final Charge charge,
            final Schedule.Status status) {
        if (!new LocalDateInterval(startDate, endDate).isValid()) {
            return "End date can not be before start date";
        }

        for (Schedule schedule : this.findScheduleByPropertyAndCharge(property, charge)) {
            if (schedule.getInterval().overlaps(new LocalDateInterval(startDate, endDate))) {
                return "A new schedule cannot overlap an existing schedule for this charge.";
            }
        }

        return null;
    }

    // //////////////////////////////////////

    @Programmatic
    public Schedule findOrCreateSchedule(
            final Property property,
            final Budget budget,
            final LocalDate startDate,
            final LocalDate endDate,
            final Charge charge,
            final Schedule.Status status) {
        if (findUniqueSchedule(property,charge,startDate,endDate)!= null) {
            return findUniqueSchedule(property,charge,startDate,endDate);
        } else {
            return newSchedule(property, budget, startDate, endDate, charge, status);
        }
    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.SAFE, restrictTo = RestrictTo.PROTOTYPING)
    public List<Schedule> allSchedules() {
        return allInstances();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_NEITHER)
    public List<Schedule> findScheduleByProperty(Property property){
        return allMatches("findByProperty", "property", property);
    };

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_NEITHER)
    public List<Schedule> findScheduleByBudget(Budget budget){
        return allMatches("findByBudget", "budget", budget);
    };

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_NEITHER)
    public List<Schedule> findScheduleByPropertyAndCharge(Property property, Charge charge){
        return allMatches("findByPropertyAndCharge", "property", property, "charge", charge);
    };

    @Programmatic
    public Schedule findUniqueSchedule(
            final Property property,
            final Charge charge,
            final LocalDate startDate,
            final LocalDate endDate){
        return firstMatch("findByPropertyChargeAndDates", "property", property, "charge", charge, "startDate", startDate, "endDate", endDate);
    }
}