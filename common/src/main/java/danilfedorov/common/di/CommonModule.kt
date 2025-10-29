package danilfedorov.common.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides

@Module
object CommonModule {

    @AppScope
    @Provides
    fun provideResources(context: Context): Resources = context.resources
}