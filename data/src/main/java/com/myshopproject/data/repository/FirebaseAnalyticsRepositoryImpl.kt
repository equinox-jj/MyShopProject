package com.myshopproject.data.repository

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.myshopproject.data.utils.Constants.BACK_ICON
import com.myshopproject.data.utils.Constants.BANK
import com.myshopproject.data.utils.Constants.BUTTON_CLICKED
import com.myshopproject.data.utils.Constants.BUTTON_NAME
import com.myshopproject.data.utils.Constants.BUY
import com.myshopproject.data.utils.Constants.CHANGE_LANGUAGE
import com.myshopproject.data.utils.Constants.CHANGE_PASSWORD
import com.myshopproject.data.utils.Constants.CHECKOUT
import com.myshopproject.data.utils.Constants.DELETE_ICON
import com.myshopproject.data.utils.Constants.DETAIL
import com.myshopproject.data.utils.Constants.EMAIL
import com.myshopproject.data.utils.Constants.FAVORITE
import com.myshopproject.data.utils.Constants.GENDER
import com.myshopproject.data.utils.Constants.HOME
import com.myshopproject.data.utils.Constants.ICON_PHOTO
import com.myshopproject.data.utils.Constants.IMAGE
import com.myshopproject.data.utils.Constants.ITEM_NAME
import com.myshopproject.data.utils.Constants.LANGUAGE
import com.myshopproject.data.utils.Constants.LOGIN
import com.myshopproject.data.utils.Constants.LOGOUT
import com.myshopproject.data.utils.Constants.LOVE_ICON
import com.myshopproject.data.utils.Constants.MESSAGE
import com.myshopproject.data.utils.Constants.MULTIPLE_SELECT
import com.myshopproject.data.utils.Constants.MULTIPLE_SELECT_ICON
import com.myshopproject.data.utils.Constants.NAME
import com.myshopproject.data.utils.Constants.NOTIFICATION
import com.myshopproject.data.utils.Constants.NOTIFICATION_ICON
import com.myshopproject.data.utils.Constants.ON_SCROLL
import com.myshopproject.data.utils.Constants.ON_SEARCH
import com.myshopproject.data.utils.Constants.PAGE
import com.myshopproject.data.utils.Constants.PAYMENT_METHOD
import com.myshopproject.data.utils.Constants.PAYMENT_SCREEN
import com.myshopproject.data.utils.Constants.PAYMENT_TYPE
import com.myshopproject.data.utils.Constants.PHONE
import com.myshopproject.data.utils.Constants.POPUP
import com.myshopproject.data.utils.Constants.POPUP_DETAIL
import com.myshopproject.data.utils.Constants.POPUP_SORT
import com.myshopproject.data.utils.Constants.PRODUCT_ID
import com.myshopproject.data.utils.Constants.PRODUCT_NAME
import com.myshopproject.data.utils.Constants.PRODUCT_PRICE
import com.myshopproject.data.utils.Constants.PRODUCT_RATE
import com.myshopproject.data.utils.Constants.PRODUCT_TOTAL
import com.myshopproject.data.utils.Constants.PRODUCT_TOTAL_PRICE
import com.myshopproject.data.utils.Constants.PROFILE
import com.myshopproject.data.utils.Constants.RATE
import com.myshopproject.data.utils.Constants.READ_ICON
import com.myshopproject.data.utils.Constants.SAVE
import com.myshopproject.data.utils.Constants.SCREEN_CLASS
import com.myshopproject.data.utils.Constants.SCREEN_NAME
import com.myshopproject.data.utils.Constants.SCREEN_VIEW
import com.myshopproject.data.utils.Constants.SEARCH
import com.myshopproject.data.utils.Constants.SELECT_ITEM
import com.myshopproject.data.utils.Constants.SHARE_PRODUCT
import com.myshopproject.data.utils.Constants.SHOW
import com.myshopproject.data.utils.Constants.SIGNUP
import com.myshopproject.data.utils.Constants.SORT_BY
import com.myshopproject.data.utils.Constants.SPLASH
import com.myshopproject.data.utils.Constants.STATUS
import com.myshopproject.data.utils.Constants.SUBMIT
import com.myshopproject.data.utils.Constants.TITLE
import com.myshopproject.data.utils.Constants.TOTAL
import com.myshopproject.data.utils.Constants.TOTAL_SELECT_ITEM
import com.myshopproject.data.utils.Constants.TROLLEY
import com.myshopproject.data.utils.Constants.TROLLEY_ICON
import com.myshopproject.data.utils.Constants.TROLLEY_PLUS
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import javax.inject.Inject

class FirebaseAnalyticsRepositoryImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
): FirebaseAnalyticsRepository {
    // Splash Screen
    override fun onSplashLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, SPLASH)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    // Login Screen
    override fun onLoginLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, LOGIN)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onLoginButtonClick(email: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, LOGIN)
            putString(EMAIL, email)
            putString(BUTTON_NAME, LOGIN)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickButtonSignUp() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, LOGIN)
            putString(BUTTON_NAME, SIGNUP)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Sign Up Screen
    override fun onSignUpLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, SIGNUP)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onButtonLoginClick() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, SIGNUP)
            putString(BUTTON_NAME, LOGIN)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onCameraButtonClick() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, SIGNUP)
            putString(BUTTON_NAME, ICON_PHOTO)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onChangeImage(image: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PROFILE)
            putString(IMAGE, image)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onButtonSignUpClick(
        image: String?,
        email: String?,
        name: String?,
        phone: String?,
        gender: String?
    ) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, SIGNUP)
            putString(IMAGE, image)
            putString(EMAIL, email)
            putString(NAME, name)
            putString(PHONE, phone)
            putString(GENDER, gender)
            putString(BUTTON_NAME, SIGNUP)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Home Screen
    override fun onHomeLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, HOME)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onPagingScroll(offset: Int?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, HOME)
            putString(PAGE, offset.toString())
        }
        firebaseAnalytics.logEvent(ON_SCROLL, bundle)
    }

    override fun onSearchHome(query: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, HOME)
            putString(SEARCH, query)
        }
        firebaseAnalytics.logEvent(ON_SEARCH, bundle)
    }

    override fun onProductHomeClick(
        productName: String?,
        productPrice: Double?,
        productRate: Int?,
        productId: Int?
    ) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, HOME)
            putString(PRODUCT_NAME, productName)
            putString(PRODUCT_PRICE, productPrice.toString())
            putString(PRODUCT_RATE, productRate.toString())
            putString(PRODUCT_ID, productId.toString())
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onClickTrolleyIcon() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, HOME)
            putString(BUTTON_NAME, TROLLEY_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickNotificationIcon() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, HOME)
            putString(BUTTON_NAME, NOTIFICATION_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Favorite Screen
    override fun onFavoriteLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, FAVORITE)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onSearchFavorite(query: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, FAVORITE)
            putString(SEARCH, query)
        }
        firebaseAnalytics.logEvent(ON_SEARCH, bundle)
    }

    override fun onSortByName(sort: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, FAVORITE)
            putString(SORT_BY, sort)
        }
        firebaseAnalytics.logEvent(POPUP_SORT, bundle)
    }

    override fun onProductFavoriteClick(
        productName: String?,
        productPrice: Double?,
        productRate: Int?,
        productId: Int?
    ) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, FAVORITE)
            putString(PRODUCT_NAME, productName)
            putString(PRODUCT_PRICE, productPrice.toString())
            putString(PRODUCT_RATE, productRate.toString())
            putString(PRODUCT_ID, productId.toString())
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    // Notification Screen
    override fun onNotificationLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, NOTIFICATION)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onMultipleSelect() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, NOTIFICATION)
            putString(BUTTON_NAME, MULTIPLE_SELECT_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickBackIconNotif() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, NOTIFICATION)
            putString(BUTTON_NAME, BACK_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickBackMultipleNotif() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, MULTIPLE_SELECT)
            putString(BUTTON_NAME, BACK_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickItemNotification(title: String?, message: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, NOTIFICATION)
            putString(TITLE, title)
            putString(MESSAGE, message)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onCheckBoxNotificationSelect(title: String?, message: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, MULTIPLE_SELECT)
            putString(TITLE, title)
            putString(MESSAGE, message)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onClickDeleteNotification(total: Int?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, MULTIPLE_SELECT)
            putString(BUTTON_NAME, DELETE_ICON)
            putString(TOTAL_SELECT_ITEM, total.toString())
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickReadNotification(total: Int?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, MULTIPLE_SELECT)
            putString(BUTTON_NAME, READ_ICON)
            putString(TOTAL_SELECT_ITEM, total.toString())
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Detail Screen
    override fun onDetailLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onClickButtonTrolley() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, TROLLEY_PLUS)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickButtonBuy() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, BUY)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickShareDetail(productName: String?, productPrice: Double?, productId: Int?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(PRODUCT_NAME, productName)
            putString(PRODUCT_PRICE, productPrice.toString())
            putString(PRODUCT_ID, productId.toString())
            putString(BUTTON_NAME, SHARE_PRODUCT)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickFavorite(productName: String?, productId: Int?, favoriteState: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, LOVE_ICON)
            putString(PRODUCT_ID, productId.toString())
            putString(PRODUCT_NAME, productName)
            putString(STATUS, favoriteState)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickBackButtonDetail() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, BACK_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Bottom Sheet
    override fun onShowBottomSheet(productId: Int?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(POPUP, SHOW)
            putString(PRODUCT_ID, productId.toString())
        }
        firebaseAnalytics.logEvent(POPUP_DETAIL, bundle)
    }

    override fun onClickQuantityBottom(
        quantity: String?,
        total: Int?,
        productId: Int?,
        productName: String?
    ) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, quantity)
            putString(TOTAL, total.toString())
            putString(PRODUCT_ID, productId.toString())
            putString(PRODUCT_NAME, productName)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickButtonBuyNow() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, "Buy Now - 10.000")
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickIconBankBottom(paymentMethod: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, paymentMethod)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickButtonBuyNowWithPayment(
        productPrice: String?,
        productId: Int?,
        productName: String?,
        productTotalPrice: Int?,
        productTotal: Int?,
        paymentMethod: String?
    ) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, DETAIL)
            putString(BUTTON_NAME, "Buy Now - 10.000")
            putString(PRODUCT_ID, productId.toString())
            putString(PRODUCT_NAME, productName)
            putString(PRODUCT_PRICE, productPrice)
            putString(PRODUCT_TOTAL, productTotal.toString())
            putString(PRODUCT_TOTAL_PRICE, productTotalPrice.toString())
            putString(PAYMENT_METHOD, paymentMethod)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Payment Screen
    override fun onPaymentLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PAYMENT_SCREEN)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onClickButtonBackPayment() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PAYMENT_SCREEN)
            putString(BUTTON_NAME, BACK_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickPaymentMethod(payment: String?, bank: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PAYMENT_SCREEN)
            putString(PAYMENT_TYPE, payment)
            putString(BANK, bank)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Trolley Screen
    override fun onTrolleyLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onClickQuantityTrolley(
        quantity: String?,
        total: Int?,
        productId: Int?,
        productName: String?
    ) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(BUTTON_NAME, quantity)
            putString(TOTAL, total.toString())
            putString(PRODUCT_ID, productId.toString())
            putString(PRODUCT_NAME, productName)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickDeleteTrolley(productId: Int?, productName: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(BUTTON_NAME, DELETE_ICON)
            putString(PRODUCT_ID, productId.toString())
            putString(PRODUCT_NAME, productName)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onCheckBoxTrolley(productId: Int?, productName: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(PRODUCT_ID, productId.toString())
            putString(PRODUCT_NAME, productName)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onClickButtonBuyTrolley() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(BUTTON_NAME, BUY)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickButtonBackTrolley() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(BUTTON_NAME, BACK_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickIconBankTrolley(paymentMethod: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(BUTTON_NAME, paymentMethod)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickButtonBuyNowWithPaymentTrolley(
        productTotalPrice: Double?,
        paymentMethod: String?
    ) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, TROLLEY)
            putString(BUTTON_NAME, BUY)
            putString(PRODUCT_TOTAL_PRICE, productTotalPrice.toString())
            putString(PAYMENT_METHOD, paymentMethod)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Success Screen
    override fun onSuccessLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, CHECKOUT)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onClickButtonSubmitSuccess(rate: Int?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, CHECKOUT)
            putString(BUTTON_NAME, SUBMIT)
            putString(RATE, rate.toString())
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Profile Screen
    override fun onProfileLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PROFILE)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onChangeLanguage(language: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PROFILE)
            putString(ITEM_NAME, CHANGE_LANGUAGE)
            putString(LANGUAGE, language)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onClickChangePassword() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PROFILE)
            putString(ITEM_NAME, CHANGE_PASSWORD)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onClickLogout() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PROFILE)
            putString(ITEM_NAME, LOGOUT)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onChangeImageProfile(image: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PROFILE)
            putString(IMAGE, image)
        }
        firebaseAnalytics.logEvent(SELECT_ITEM, bundle)
    }

    override fun onClickCameraIconProfile() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, PROFILE)
            putString(BUTTON_NAME, ICON_PHOTO)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    // Change Password Screen
    override fun onChangeLoadScreen(nameClass: String?) {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, CHANGE_PASSWORD)
            putString(SCREEN_CLASS, nameClass)
        }
        firebaseAnalytics.logEvent(SCREEN_VIEW, bundle)
    }

    override fun onClickButtonSavePass() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, CHANGE_PASSWORD)
            putString(BUTTON_NAME, SAVE)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }

    override fun onClickButtonBackPass() {
        val bundle = Bundle().apply {
            putString(SCREEN_NAME, CHANGE_PASSWORD)
            putString(BUTTON_NAME, BACK_ICON)
        }
        firebaseAnalytics.logEvent(BUTTON_CLICKED, bundle)
    }
}