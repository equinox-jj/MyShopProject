package com.myshopproject.domain.repository

interface FirebaseAnalyticsRepository {
    // Splash Screen
    fun onSplashLoadScreen(nameClass: String)

    // Login Screen
    fun onLoginLoadScreen(nameClass: String)
    fun onLoginButtonClick(email: String)
    fun onClickButtonSignUp()

    // Sign Up Screen
    fun onSignUpLoadScreen(nameClass: String)
    fun onButtonLoginClick()
    fun onCameraButtonClick()
    fun onChangeImage(image: String)
    fun onButtonSignUpClick(image: String, email: String, name: String, phone: String, gender: String)

    // Home Screen
    fun onHomeLoadScreen(nameClass: String)
    fun onPagingScroll(offset: Int)
    fun onSearchHome(query: String)
    fun onProductHomeClick(productName: String, productPrice: Double, productRate: Int, productId: Int)
    fun onClickTrolleyIconHome()
    fun onClickNotificationIconHome()

    // Favorite Screen
    fun onFavoriteLoadScreen(nameClass: String)
    fun onSearchFavorite(query: String)
    fun onSortByName(sort: String)
    fun onProductFavoriteClick(productName: String, productPrice: Double, productRate: Int, productId: Int)

    // Notification Screen
    fun onNotificationLoadScreen(nameClass: String)
    fun onMultipleSelect()
    fun onClickBackIconNotif()
    fun onClickBackMultipleNotif()
    fun onClickItemNotification(title: String, message: String)
    fun onCheckBoxNotificationSelect(title: String, message: String)
    fun onClickDeleteNotification(total: Int)
    fun onClickReadNotification(total: Int)

    // Detail Screen
    fun onDetailLoadScreen(nameClass: String)
    fun onClickButtonTrolley()
    fun onClickButtonBuy()
    fun onClickShareDetail(productName: String, productPrice: Double, productId: Int)
    fun onClickFavorite(productName: String, productId: Int, favoriteState: String)
    fun onClickBackButtonDetail()

    // Bottom Sheet
    fun onShowBottomSheet(productId: Int)
    fun onClickQuantityBottom(quantity: String, total: Int, productId: Int, productName: String)
    fun onClickButtonBuyNow()
    fun onClickIconBankBottom(paymentMethod: String)
    fun onClickButtonBuyNowWithPayment(productPrice: String, productId: Int, productName: String, productTotalPrice: Int, productTotal: Int, paymentMethod: String)

    // Payment Screen
    fun onPaymentLoadScreen(nameClass: String)
    fun onClickButtonBackPayment()
    fun onClickPaymentMethod(payment: String, bank: String)

    // Trolley Screen
    fun onTrolleyLoadScreen(nameClass: String)
    fun onClickQuantityTrolley(quantity: String, total: Int, productId: Int, productName: String)
    fun onClickDeleteTrolley(productId: Int, productName: String)
    fun onCheckBoxTrolley(productId: Int, productName: String)
    fun onClickButtonBuyTrolley()
    fun onClickButtonBackTrolley()
    fun onClickIconBankTrolley(paymentMethod: String)
    fun onClickButtonBuyNowWithPayment(productTotalPrice: Double, paymentMethod: String)

    // Success Activity
    fun onSuccessLoadScreen(nameClass: String)
    fun onClickButtonSubmitSuccess(rate: Int)

    // Profile Page Screen
    fun onProfileLoadScreen(nameClass: String)
    fun onChangeLanguage(language: String)
    fun onClickChangePassword()
    fun onClickLogout()
    fun onChangeImageProfile(image: String)
    fun onClickCameraIconProfile()

    // Change Password Screen
    fun onChangeLoadScreen(nameClass: String)
    fun onClickButtonSavePass()
    fun onClickButtonBackPass()
}