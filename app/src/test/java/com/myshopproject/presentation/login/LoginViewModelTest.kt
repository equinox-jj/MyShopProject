package com.myshopproject.presentation.login

//@ExperimentalCoroutinesApi
//@RunWith(MockitoJUnitRunner::class)
//class LoginViewModelTest {

    /*@get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var remoteUseCase: RemoteUseCase
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(remoteUseCase)
    }

    @Test
    fun `test login function when state is success`() = runTest {
        val dummyData = dummyLoginResponse()

        val email = ArgumentMatchers.anyString()
        val password = ArgumentMatchers.anyString()
        val firebaseToken = ArgumentMatchers.anyString()

        // Arrange
        Mockito.`when`(
            remoteUseCase.loginAccount(
                email = email,
                password = password,
                firebaseToken = firebaseToken
            )
        ).thenReturn(flowOf(Resource.Success(dummyData.success)))

        // Act
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            loginViewModel.state.value
        }
        advanceUntilIdle()

        // Assert
        assertEquals(Resource.Success(dummyData), loginViewModel.state.value)
    }*/
//}