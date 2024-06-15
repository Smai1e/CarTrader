package dev.smai1e.carTrader.data

import dev.smai1e.carTrader.domain.errorTypes.DataError

/**
 * Merges data received from different sources.
 */
interface MergeStrategy<T : Any, O : Any, E : DataError> {

    /**
     * Merging the same data types.
     */
    fun mergeSame(
        left: RequestResult<T, E>,
        right: RequestResult<T, E>
    ): RequestResult<T, DataError>

    /**
     * Merging different types of data.
     */
    fun mergeDifferent(
        left: RequestResult<T, E>,
        right: RequestResult<O, E>
    ): RequestResult<T, DataError>
}

class ResponseMergeStrategy<T : Any, O : Any, E : DataError> : MergeStrategy<T, O, E> {
    override fun mergeSame(
        left: RequestResult<T, E>,
        right: RequestResult<T, E>
    ): RequestResult<T, DataError> {
        return when {
            left is RequestResult.Loading && right is RequestResult.Loading ->
                merge(left, right)

            left is RequestResult.Loading && right is RequestResult.Success ->
                merge(left, right)

            left is RequestResult.Loading && right is RequestResult.Error ->
                merge(left, right)

            left is RequestResult.Success && right is RequestResult.Loading ->
                merge(left, right)

            left is RequestResult.Success && right is RequestResult.Success ->
                merge(left, right)

            left is RequestResult.Success && right is RequestResult.Error ->
                merge(left, right)

            else -> throw IllegalStateException("Unimplemented branch")
        }
    }

    override fun mergeDifferent(
        left: RequestResult<T, E>,
        right: RequestResult<O, E>
    ): RequestResult<T, DataError> {
        throw IllegalStateException("Unimplemented branch")
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Loading,
        right: RequestResult.Loading
    ): RequestResult<T, E> {
        return RequestResult.Loading
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Loading,
        right: RequestResult.Success<T, E>
    ): RequestResult<T, E> {
        return RequestResult.Success(right.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Loading,
        right: RequestResult.Error<T, E>
    ): RequestResult<T, E> {
        return RequestResult.Error(right.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Success<T, E>,
        right: RequestResult.Loading
    ): RequestResult<T, E> {
        return RequestResult.Success(left.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Success<T, E>,
        right: RequestResult.Success<T, E>
    ): RequestResult<T, E> {
        return RequestResult.Success(right.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Success<T, E>,
        right: RequestResult.Error<T, E>
    ): RequestResult<T, E> {
        return RequestResult.Error(right.error)
    }
}

class BidsMergeStrategy<T : Any, M : Any, E : DataError> : MergeStrategy<T, M, E> {
    override fun mergeDifferent(
        left: RequestResult<T, E>,
        right: RequestResult<M, E>
    ): RequestResult<T, DataError> {
        return when {
            left is RequestResult.Loading && right is RequestResult.Loading ->
                merge(left, right)

            left is RequestResult.Success && right is RequestResult.Loading ->
                merge(left, right)

            left is RequestResult.Error && right is RequestResult.Loading ->
                merge(left, right)

            left is RequestResult.Loading && right is RequestResult.Success ->
                merge(left, right)

            left is RequestResult.Success && right is RequestResult.Success ->
                merge(left, right)

            left is RequestResult.Error && right is RequestResult.Success ->
                merge(left, right)

            left is RequestResult.Loading && right is RequestResult.Error ->
                merge(left, right)

            left is RequestResult.Success && right is RequestResult.Error ->
                merge(left, right)

            left is RequestResult.Error && right is RequestResult.Error ->
                merge(left, right)

            else -> throw IllegalStateException("Unimplemented branch")
        }
    }

    override fun mergeSame(
        left: RequestResult<T, E>,
        right: RequestResult<T, E>
    ): RequestResult<T, DataError> {
        throw IllegalStateException("Unimplemented branch")
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Loading,
        right: RequestResult.Loading
    ): RequestResult<T, E> {
        return RequestResult.Loading
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Success<T, E>,
        right: RequestResult.Loading
    ): RequestResult<T, E> {
        return RequestResult.Success(left.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Error<T, E>,
        right: RequestResult.Loading
    ): RequestResult<T, E> {
        return RequestResult.Error(left.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Loading,
        right: RequestResult.Success<M, E>
    ): RequestResult<T, E> {
        return RequestResult.Loading
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Success<T, E>,
        right: RequestResult.Success<M, E>
    ): RequestResult<T, E> {
        return RequestResult.Success(left.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Error<T, E>,
        right: RequestResult.Success<M, E>
    ): RequestResult<T, E> {
        return RequestResult.Error(left.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Loading,
        right: RequestResult.Error<M, E>
    ): RequestResult<T, E> {
        return RequestResult.Error(right.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Success<T, E>,
        right: RequestResult.Error<M, E>
    ): RequestResult<T, E> {
        return RequestResult.Error(right.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        left: RequestResult.Error<T, E>,
        right: RequestResult.Error<M, E>
    ): RequestResult<T, E> {
        return RequestResult.Error(left.error)
    }
}