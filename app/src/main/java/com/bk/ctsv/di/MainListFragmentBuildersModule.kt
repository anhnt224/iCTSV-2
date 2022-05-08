/*
 * Copyright 2018-2019 Andrius Baruckis www.baruckis.com | mycryptocoins.baruckis.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bk.ctsv.di


import com.bk.ctsv.teacher.fragment.THome2Fragment
import com.bk.ctsv.ui.fragments.Home2Fragment
import com.bk.ctsv.ui.fragments.HomeFragment
import com.bk.ctsv.ui.fragments.activity.*
import com.bk.ctsv.ui.fragments.criteria.CriteriaFragment
import com.bk.ctsv.ui.fragments.criteria.TrainingPointFragment
import com.bk.ctsv.ui.fragments.form.*
import com.bk.ctsv.ui.fragments.gift.*
import com.bk.ctsv.ui.fragments.job.ApplyJobFragment
import com.bk.ctsv.ui.fragments.job.JobDetailFragment
import com.bk.ctsv.ui.fragments.job.ListJobApplyFragment
import com.bk.ctsv.ui.fragments.job.ListJobsFragment
import com.bk.ctsv.ui.fragments.motel.SearchMotelFragment
import com.bk.ctsv.ui.fragments.running.RunDashboardFragment
import com.bk.ctsv.ui.fragments.running.RunFragment
import com.bk.ctsv.ui.fragments.running.RunningDataFragment
import com.bk.ctsv.ui.fragments.running.RunningFragment
import com.bk.ctsv.ui.fragments.scholarShip.ListScholarShipAppliedFragment
import com.bk.ctsv.ui.fragments.scholarShip.ListScholarShipsFragment
import com.bk.ctsv.ui.fragments.scholarShip.ScholarShipDetailFragment
import com.bk.ctsv.ui.fragments.timetable.TimeTableFragment
import com.bk.ctsv.ui.fragments.user.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * All fragments related to MainActivity intended to use Dagger @Inject should be listed here.
 */
@Module
abstract class MainListFragmentBuildersModule {


    //activity


    @ContributesAndroidInjector()
    abstract fun contributeUserCheckInActivityInfoFragment(): UserCheckInActivityInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeActivityDetailByUserFragment(): ActivityDetailByUserUnitFragment

    //user


    @ContributesAndroidInjector()
    abstract fun contributeScheduleFragment(): ScheduleFragment

    @ContributesAndroidInjector()
    abstract fun contributeMessageListFragment(): MessageListFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector()
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector()
    abstract fun contributeUserInfoFragment(): UserInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeListActivityFragment(): ListActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeListScholarShipsFragment(): ListScholarShipsFragment

    @ContributesAndroidInjector
    abstract fun contributeListScholarShipAppliedFragment(): ListScholarShipAppliedFragment

    @ContributesAndroidInjector
    abstract fun contributeScholarShipDetailFragment(): ScholarShipDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeListJobsFragment(): ListJobsFragment

    @ContributesAndroidInjector
    abstract fun contributeJobDetailFragment(): JobDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeListJobApply(): ListJobApplyFragment

    @ContributesAndroidInjector
    abstract fun contributeApplyJob(): ApplyJobFragment

    @ContributesAndroidInjector
    abstract fun contributeQRStudent(): QrStudentFragment

    @ContributesAndroidInjector
    abstract fun contributeListAddressFragment(): ListAddressFragment

    @ContributesAndroidInjector
    abstract fun contributeAddNewAddressFragment(): AddNewAddressFragment

    @ContributesAndroidInjector
    abstract fun contributeListFormsFragment(): ListFormsFragment

    @ContributesAndroidInjector
    abstract fun contributeListFormsRegisteredFragment(): ListFormsRegisteredFragment

    @ContributesAndroidInjector
    abstract fun contributeFormRegisteredDetailFragment(): FormRegisteredDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFormFragment(): RegisterFormFragment

    @ContributesAndroidInjector
    abstract fun contributeTrainingPointFragment(): TrainingPointFragment

    @ContributesAndroidInjector
    abstract fun contributeCriteriaFragment():CriteriaFragment

    @ContributesAndroidInjector
    abstract fun contributeEditFormFragment(): EditFormFragment

    @ContributesAndroidInjector
    abstract fun contributeTimeTableFragment(): TimeTableFragment

    @ContributesAndroidInjector
    abstract fun contributeNotesFragment(): NotesFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseDeliveryTypeFragment(): ChooseDeliveryTypeFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseReceiverAddressFragment(): ChooseReceiverAddressFragment

    @ContributesAndroidInjector
    abstract fun contributeRunningFragment(): RunningFragment

    @ContributesAndroidInjector
    abstract fun contributeRunningDataFragment(): RunningDataFragment

    @ContributesAndroidInjector
    abstract fun contributeRunDashboardFragment(): RunDashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeRunFragment(): RunFragment

    @ContributesAndroidInjector
    abstract fun contributeHome2Fragment(): Home2Fragment

    @ContributesAndroidInjector
    abstract fun contributeGiftFragment(): GiftFragment

    @ContributesAndroidInjector
    abstract fun contributeGiftInfoFragment(): GiftInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterGiftFragment(): ApplyGiftFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateGiftFragment(): CreateGiftFragment

    @ContributesAndroidInjector
    abstract fun contributeGiftGivenFragment(): GiftGivenFragment

    @ContributesAndroidInjector
    abstract fun contributeListRegisterFragment(): ListRegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeReceiverAddressFragment(): ReceiverAddressFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchMotelFragment(): SearchMotelFragment
}