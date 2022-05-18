package com.bk.ctsv.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bk.ctsv.teacher.fragment.gift.TGiftInfoViewModel
import com.bk.ctsv.teacher.viewmodel.THome2ViewModel
import com.bk.ctsv.teacher.viewmodel.THomeViewModel
import com.bk.ctsv.teacher.viewmodel.TListNotificationsViewModel
import com.bk.ctsv.teacher.viewmodel.account.TAccountViewModel
import com.bk.ctsv.teacher.viewmodel.account.TChangePasswordViewModel
import com.bk.ctsv.teacher.viewmodel.account.TeacherInfoViewModel
import com.bk.ctsv.teacher.viewmodel.activity.TActivityInfoViewModel
import com.bk.ctsv.teacher.viewmodel.activity.TListActivitiesViewModel
import com.bk.ctsv.teacher.viewmodel.form.*
import com.bk.ctsv.teacher.viewmodel.gift.TCreateGiftViewModel
import com.bk.ctsv.teacher.viewmodel.gift.TGiftGivenViewModel
import com.bk.ctsv.teacher.viewmodel.gift.TGiftViewModel
import com.bk.ctsv.teacher.viewmodel.gift.TListRegisterViewModel
import com.bk.ctsv.teacher.viewmodel.job.TListJobsViewModel
import com.bk.ctsv.teacher.viewmodel.scholarShip.TListScholarShipsViewModel
import com.bk.ctsv.teacher.viewmodel.student.*
import com.bk.ctsv.ui.fragments.Home2ViewModel
import com.bk.ctsv.ui.fragments.motel.MotelInfoFragment
import com.bk.ctsv.ui.viewmodels.gift.ReceiverAddressViewModel
import com.bk.ctsv.ui.fragments.running.RunViewModel
import com.bk.ctsv.ui.viewmodels.running.RunDashboardViewModel
import com.bk.ctsv.ui.viewmodels.criteria.TrainingPointViewModel
import com.bk.ctsv.ui.viewmodels.job.ListJobApplyViewModel
import com.bk.ctsv.ui.viewmodels.HomeViewModel
import com.bk.ctsv.ui.viewmodels.activity.*
import com.bk.ctsv.ui.viewmodels.criteria.CriteriaViewModel
import com.bk.ctsv.ui.viewmodels.form.*
import com.bk.ctsv.ui.viewmodels.gift.*
import com.bk.ctsv.ui.viewmodels.job.ApplyJobViewModel
import com.bk.ctsv.ui.viewmodels.job.JobDetailViewModel
import com.bk.ctsv.ui.viewmodels.job.ListJobsViewModel
import com.bk.ctsv.ui.viewmodels.motel.AddMotelInfoViewModel
import com.bk.ctsv.ui.viewmodels.motel.ImageMotelViewModel
import com.bk.ctsv.ui.viewmodels.motel.MotelInfoViewModel
import com.bk.ctsv.ui.viewmodels.motel.SearchMotelViewModel
import com.bk.ctsv.ui.viewmodels.running.RunningDataViewModel
import com.bk.ctsv.ui.viewmodels.running.RunningViewModel
import com.bk.ctsv.ui.viewmodels.scholarShip.ListScholarShipAppliedViewModel
import com.bk.ctsv.ui.viewmodels.scholarShip.ListScholarShipsViewModel
import com.bk.ctsv.ui.viewmodels.scholarShip.ScholarShipDetailViewModel
import com.bk.ctsv.ui.viewmodels.timetable.TimeTableViewModel
import com.bk.ctsv.ui.viewmodels.user.*

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    // We'd like to take this implementation of the ViewModel class and make it available in an injectable map with MainViewModel::class as a key to that map.


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    //activity

    @Binds
    @IntoMap
    @ViewModelKey(ActivityDetailByUserUnitViewModel::class)
    abstract fun bindActivityDetailByUserUnitViewModel(activityDetailByUserUnitViewModel: ActivityDetailByUserUnitViewModel): ViewModel


    //user

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleViewModel::class)
    abstract fun bindScheduleViewModel(scheduleViewModel: ScheduleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessageListViewModel::class)
    abstract fun bindMessageListViewModel(messageListViewModel: MessageListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(UserCheckInActivityViewModel::class)
    abstract fun bindUserCheckInActivityViewModel(userCheckInActivityViewModel: UserCheckInActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun bindChangePasswordViewModel(changePasswordViewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LostPasswordViewModel::class)
    abstract fun bindLostPasswordViewModel(lostPasswordViewModel: LostPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckOTPLostPasswordViewModel::class)
    abstract fun bindCheckOTPLostPasswordViewModel(checkOTPLostPasswordViewModel: CheckOTPLostPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserInfoViewModel::class)
    abstract fun bindUserInfoViewModel(userInfoViewModel: UserInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListActivityViewModel::class)
    abstract fun bindListActivityViewModel(viewModel: ListActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListScholarShipsViewModel::class)
    abstract fun bindListScholarShipsViewModel(viewModel: ListScholarShipsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListScholarShipAppliedViewModel::class)
    abstract fun bindListScholarShipsAppliedViewModel(viewModel: ListScholarShipAppliedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScholarShipDetailViewModel ::class)
    abstract fun bindScholarShipDetailViewModel(viewModel: ScholarShipDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListJobsViewModel ::class)
    abstract fun bindListJobsViewModel(viewModel: ListJobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobDetailViewModel ::class)
    abstract fun bindJobDetailViewModel(viewModel: JobDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListJobApplyViewModel::class)
    abstract fun bindListJobApplyViewModel(viewModel: ListJobApplyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApplyJobViewModel::class)
    abstract fun bindApplyJobViewModel(viewModel: ApplyJobViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QrStudentViewModel::class)
    abstract fun bindQRStudentViewModel(viewModel: QrStudentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ListAddressViewModel::class)
    abstract fun bindListAddressViewModel(viewModel: ListAddressViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddNewAddressViewModel::class)
    abstract fun bindAddNewAddressViewModel(viewModel: AddNewAddressViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListFormsViewModel::class)
    abstract fun bindListFormsViewModel(viewModel: ListFormsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FormRegisteredDetailViewModel::class)
    abstract fun bindFormRegisteredDetailViewModel(viewModel: FormRegisteredDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListFormsRegisteredViewModel::class)
    abstract fun bindListFormsRegisteredViewModel(viewModel: ListFormsRegisteredViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterFormViewModel::class)
    abstract fun bindRegisterFormViewModel(viewModel: RegisterFormViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrainingPointViewModel::class)
    abstract fun bindTrainingPointViewModel(viewModel: TrainingPointViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CriteriaViewModel::class)
    abstract fun bindCriteriaViewModel(viewModel: CriteriaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditFormViewModel::class)
    abstract fun bindEditFormViewModel(viewModel: EditFormViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimeTableViewModel::class)
    abstract fun bindTimeTableViewModel(viewModel: TimeTableViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(THomeViewModel::class)
    abstract fun bindTHomeViewModel(viewModel: THomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TListFormViewModel::class)
    abstract fun bindTListFormViewModel(viewModel: TListFormViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TRegisterFormViewModel::class)
    abstract fun bindTRegisterFormViewModel(viewModel: TRegisterFormViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TListFormRegisteredViewModel::class)
    abstract fun bindTListFormRegisteredViewModel(viewModel: TListFormRegisteredViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TFormRegisteredViewModel::class)
    abstract fun bindTFormRegisteredViewModel(viewModel: TFormRegisteredViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TEditFormViewModel::class)
    abstract fun bindTEditFormViewModel(viewModel: TEditFormViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TListActivitiesViewModel::class)
    abstract fun bindTListActivitiesViewModel(viewModel: TListActivitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TActivityInfoViewModel::class)
    abstract fun bindTActivityInfoViewModel(viewModel: TActivityInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TListScholarShipsViewModel::class)
    abstract fun bindTListScholarShipsViewModel(viewModel: TListScholarShipsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TListJobsViewModel::class)
    abstract fun bindTListJobsViewModel(viewModel: TListJobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TListNotificationsViewModel::class)
    abstract fun bindTListNotificationsViewModel(viewModel: TListNotificationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TAccountViewModel::class)
    abstract fun bindTAccountViewModel(viewModel: TAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TChangePasswordViewModel::class)
    abstract fun bindTChangePassword(ViewModel: TChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TeacherInfoViewModel::class)
    abstract fun bindTeacherInfoViewModel(viewModel: TeacherInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListStudentViewModel::class)
    abstract fun bindListStudentViewModel(viewModel: ListStudentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListActivitiesOfStudentViewModel::class)
    abstract fun bindListActivitiesOfStudent(viewModel: ListActivitiesOfStudentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StudentInfoViewModel::class)
    abstract fun bindStudentInfoViewModel(viewModel: StudentInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TeacherMarkViewModel::class)
    abstract fun bindTeacherMarkViewModel(viewModel: TeacherMarkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    abstract fun bindNotesViewModel(viewModel: NotesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TStudentNoteViewModel::class)
    abstract fun bindTStudentNoteViewModel(viewModel: TStudentNoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseDeliveryTypeViewModel::class)
    abstract fun bindChooseDeliveryTypeViewModel(viewModel: ChooseDeliveryTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseReceiverAddressViewModel::class)
    abstract fun bindChooseReceiverAddressViewModel(viewModel: ChooseReceiverAddressViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RunningViewModel::class)
    abstract fun bindRunningViewModel(viewModel: RunningViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RunningDataViewModel::class)
    abstract fun bindRunningDataViewModel(viewModel: RunningDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RunDashboardViewModel::class)
    abstract fun bindRunDashboardViewModel(viewModel: RunDashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RunViewModel::class)
    abstract fun bindRunViewModel(viewModel: RunViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(Home2ViewModel::class)
    abstract fun bindHome2ViewModel(viewModel: Home2ViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GiftViewModel::class)
    abstract fun bindGiftViewModel(viewModel: GiftViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GiftInfoViewModel::class)
    abstract fun bindGiftInfoViewModel(viewModel: GiftInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApplyGiftViewModel::class)
    abstract fun bindRegisterGiftViewModel(viewModel: ApplyGiftViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateGiftViewModel::class)
    abstract fun bindCreateGiftViewModel(viewModel: CreateGiftViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GiftGivenViewModel::class)
    abstract fun bindGiftGivenViewModel(viewModel: GiftGivenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListRegisterViewModel::class)
    abstract fun bindListRegisterViewModel(viewModel: ListRegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(THome2ViewModel::class)
    abstract fun bindTHome2ViewModel(viewModel: THome2ViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TGiftGivenViewModel::class)
    abstract fun bindTGiftGiven(viewModel: TGiftGivenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TCreateGiftViewModel::class)
    abstract fun bindTCreateGift(viewModel: TCreateGiftViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TListRegisterViewModel::class)
    abstract fun bindTListRegister(viewModel: TListRegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReceiverAddressViewModel::class)
    abstract fun contributeReceiverAddress(viewModel: ReceiverAddressViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TGiftViewModel::class)
    abstract fun contributeTGiftViewModel(viewModel: TGiftViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TGiftInfoViewModel::class)
    abstract fun contributeTGiftInfoViewModel(viewModel: TGiftInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchMotelViewModel::class)
    abstract fun contributeSearchMotelViewModel(viewModel: SearchMotelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddMotelInfoViewModel::class)
    abstract fun contributeAddMotelInfoViewModel(viewModel: AddMotelInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageMotelViewModel::class)
    abstract fun contributeImageMotelViewModel(viewModel: ImageMotelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MotelInfoViewModel::class)
    abstract fun contributeMotelInfoViewModel(viewModel: MotelInfoViewModel): ViewModel
}