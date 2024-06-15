package dev.smai1e.carTrader.di

import android.content.Context
import dev.smai1e.carTrader.ui.views.profile.CacheBottomDialog
import dev.smai1e.carTrader.ui.views.carPicker.CarPickerFragment
import dev.smai1e.carTrader.ui.views.profile.ReplenishmentBottomDialog
import dev.smai1e.carTrader.ui.views.profile.ProfileFragment
import dev.smai1e.carTrader.ui.views.profile.WithdrawalBottomDialog
import dev.smai1e.carTrader.ui.views.auth.SignInFragment
import dev.smai1e.carTrader.ui.views.auth.SignUpFragment
import dev.smai1e.carTrader.ui.views.auctionsList.AuctionsListFragment
import dev.smai1e.carTrader.ui.views.filterPage.FilterFragment
import dev.smai1e.carTrader.ui.views.auctionDetails.AuctionDetailsFragment
import dev.smai1e.carTrader.ui.views.ownAuctions.OwnAuctionsFragment
import dev.smai1e.carTrader.ui.views.ownAuctionDetails.OwnAuctionDetailsFragment
import dev.smai1e.carTrader.ui.views.participateAuctions.AuctionsParticipantFragment
import dev.smai1e.carTrader.ui.views.newLot.NewLotFragment
import dev.smai1e.carTrader.ui.views.splash.SplashFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment: ReplenishmentBottomDialog)
    fun inject(fragment: WithdrawalBottomDialog)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: AuctionsListFragment)
    fun inject(fragment: CarPickerFragment)
    fun inject(fragment: FilterFragment)
    fun inject(fragment: AuctionDetailsFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: NewLotFragment)
    fun inject(fragment: OwnAuctionsFragment)
    fun inject(fragment: OwnAuctionDetailsFragment)
    fun inject(fragment: AuctionsParticipantFragment)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: CacheBottomDialog)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun applicationScope(scope: CoroutineScope): Builder

        fun build(): AppComponent
    }
}

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class,
        AppBindModule::class,
        DispatcherModule::class
    ]
)
class AppModule