package danilfedorov.feature_add_product.internal.di

import dagger.Module
import dagger.Provides
import danilfedorov.common.di.FeatureScope
import danilfedorov.domain_core.repository.PremiumRepository
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ProductSuggestionRepository
import danilfedorov.domain_core.usecase.premium.PremiumStatusFlowUseCase
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.ObserveProductListByListIdUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertProductsFromNamesUseCase
import danilfedorov.domain_core.usecase.suggestion.InsertOrIncrementSuggestionUseCase
import danilfedorov.domain_core.usecase.suggestion.SearchProductSuggestionsUseCase

@Module
object UseCaseModule {

    @Provides
    @FeatureScope
    fun provideSearchProductSuggestionsUseCase(repository: ProductSuggestionRepository) =
        SearchProductSuggestionsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideDeleteProductsUseCase(repository: ProductRepository) =
        DeleteProductsUseCase(repository)

    @Provides
    @FeatureScope
    fun provideObserveProductListByListIdUseCase(repository: ProductRepository) =
        ObserveProductListByListIdUseCase(repository)

    @Provides
    @FeatureScope
    fun provideInsertProductsFromNamesUseCase(repository: ProductRepository) =
        InsertProductsFromNamesUseCase(repository)

    @Provides
    @FeatureScope
    fun provideSaveProductSuggestionsUseCase(repository: ProductSuggestionRepository) =
        InsertOrIncrementSuggestionUseCase(repository)

    @Provides
    @FeatureScope
    fun providePremiumStatusFlowUseCase(premiumRepository: PremiumRepository) =
        PremiumStatusFlowUseCase(premiumRepository)
}
