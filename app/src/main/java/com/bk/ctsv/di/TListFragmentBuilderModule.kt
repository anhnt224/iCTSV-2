package com.bk.ctsv.di

import com.bk.ctsv.teacher.fragment.THome2Fragment
import com.bk.ctsv.teacher.fragment.THomeFragment
import com.bk.ctsv.teacher.fragment.TListNotificationsFragment
import com.bk.ctsv.teacher.fragment.account.TAccountFragment
import com.bk.ctsv.teacher.fragment.account.TChangePasswordFragment
import com.bk.ctsv.teacher.fragment.account.TeacherInfoFragment
import com.bk.ctsv.teacher.fragment.activity.TActivityInfoFragment
import com.bk.ctsv.teacher.fragment.activity.TListActivitiesFragment
import com.bk.ctsv.teacher.fragment.form.*
import com.bk.ctsv.teacher.fragment.gift.*
import com.bk.ctsv.teacher.fragment.job.TListJobsFragment
import com.bk.ctsv.teacher.fragment.scholarShip.TListScholarShipsFragment
import com.bk.ctsv.teacher.fragment.student.*
import com.bk.ctsv.ui.fragments.form.ListFormsFragment
import com.bk.ctsv.ui.fragments.running.RunDashboardFragment
import com.bk.ctsv.ui.fragments.running.RunFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TListFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeTHomeFragment(): THomeFragment

    @ContributesAndroidInjector
    abstract fun contributeListFormFragment(): ListFormsFragment

    @ContributesAndroidInjector
    abstract fun contributeTListFormFragment(): TListFormFragment

    @ContributesAndroidInjector
    abstract fun contributeTRegisterFormFragment(): TRegisterFormFragment

    @ContributesAndroidInjector
    abstract fun contributeTListFormRegisteredFragment(): TListFormRegisteredFragment

    @ContributesAndroidInjector
    abstract fun contributeTFormRegisteredFragment(): TFormRegisteredFragment

    @ContributesAndroidInjector
    abstract fun contributeTEditFormFragment(): TEditFormFragment

    @ContributesAndroidInjector
    abstract fun contributeTListActivitiesFragment(): TListActivitiesFragment

    @ContributesAndroidInjector
    abstract fun contributeTActivityInfoFragment(): TActivityInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeTListScholarShipFragment(): TListScholarShipsFragment

    @ContributesAndroidInjector
    abstract fun contributeTListJobsFragment(): TListJobsFragment

    @ContributesAndroidInjector
    abstract fun contributeTListNotificationsFragment(): TListNotificationsFragment

    @ContributesAndroidInjector
    abstract fun contributeTAccountFragment(): TAccountFragment

    @ContributesAndroidInjector
    abstract fun contributeTChangePassword(): TChangePasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeTeacherInfoFragment(): TeacherInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeListStudentFragment(): ListStudentFragment

    @ContributesAndroidInjector
    abstract fun contributeListActivitiesOfStudent(): ListActivitiesOfStudentFragment

    @ContributesAndroidInjector
    abstract fun contributeStudentInfoFragment(): StudentInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeTeacherMarkFragment(): TeacherMarkFragment

    @ContributesAndroidInjector
    abstract fun contributeTStudentNoteFragment(): TStudentNoteFragment

    @ContributesAndroidInjector
    abstract fun contributeRunDashboardFragment(): RunDashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeRunFragment(): RunFragment

    @ContributesAndroidInjector
    abstract fun contributeTHome2Fragment(): THome2Fragment

    @ContributesAndroidInjector
    abstract fun contributeTGiftGivenFragment(): TGiftGivenFragment

    @ContributesAndroidInjector
    abstract fun contributeTListRegister(): TListRegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeTCreateGift(): TCreateGiftFragment

    @ContributesAndroidInjector
    abstract fun contributeTGift(): TGiftFragment

    @ContributesAndroidInjector
    abstract fun contributeTGiftInfo(): TGiftInfoFragment
}