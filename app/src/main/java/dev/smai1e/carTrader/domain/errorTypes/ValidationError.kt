package dev.smai1e.carTrader.domain.errorTypes

/**
 * Errors that occur during data validation.
 */
sealed interface ValidationError: RootError {

    enum class PasswordError : ValidationError {
        TOO_SHORT,
        NO_UPPERCASE,
        NO_DIGIT
    }

    enum class PhoneError: ValidationError {
        INCORRECT_PHONE
    }

    enum class EmailError: ValidationError {
        INCORRECT_EMAIL
    }

    enum class UserInfoError: ValidationError {
        INCORRECT_FIRST_NAME,
        INCORRECT_MIDDLE_NAME,
        INCORRECT_LAST_NAME
    }

    enum class AuctionDataError : ValidationError {
        INCORRECT_OPEN_DATE,
        INCORRECT_CLOSE_DATE,
        EMPTY_BRAND,
        EMPTY_MODEL,
        INCORRECT_VIN,
        EMPTY_DESCRIPTION,
        EMPTY_MIN_BID,
        EMPTY_COLOR,
        EMPTY_MILEAGE,
        EMPTY_GEARBOX,
        EMPTY_DRIVE_WHEEL,
        EMPTY_HORSEPOWER,
        INCORRECT_MANUFACTURER_DATE
    }
}