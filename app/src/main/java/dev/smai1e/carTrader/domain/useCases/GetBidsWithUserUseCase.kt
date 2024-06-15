package dev.smai1e.carTrader.domain.useCases

import dev.smai1e.carTrader.data.toBidWithUser
import dev.smai1e.carTrader.di.DefaultDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.Bid
import dev.smai1e.carTrader.domain.models.BidWithUser
import dev.smai1e.carTrader.domain.models.UserInfo
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.domain.repositoryInterfaces.BidsRepository
import dev.smai1e.carTrader.domain.repositoryInterfaces.RequestResultFlow
import dev.smai1e.carTrader.domain.repositoryInterfaces.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * A use case that gets a list of bidders
 * with their bids for a particular auction.
 */
class GetBidsWithUserUseCase @Inject constructor(
    private val bidsRepository: BidsRepository,
    private val userRepository: UserRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    operator fun invoke(auctionId: Long): RequestResultFlow<List<BidWithUser>, DataError> {
        val bidsRequest = bidsRepository.fetchBidsByAuctionId(auctionId)
        val usersRequest = userRepository.fetchBiddersByAuctionId(auctionId)

        return bidsRequest
            .combine(usersRequest, ::merge)
            .flowOn(defaultDispatcher)
    }

    private fun merge(
        bids: RequestResult<List<Bid>, DataError>,
        users: RequestResult<List<UserInfo>, DataError>
    ): RequestResult<List<BidWithUser>, DataError> {

        return bids.map { bids ->
            if (users is RequestResult.Success && users.data.isNotEmpty()) {
                bids.map { bid -> bid.toBidWithUser(users.data) }
            } else {
                emptyList()
            }
        }
    }
}