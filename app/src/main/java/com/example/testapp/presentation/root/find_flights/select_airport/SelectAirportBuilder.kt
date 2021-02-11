package com.example.testapp.presentation.root.find_flights.select_airport

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.testapp.R
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link SelectAirportScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class SelectAirportBuilder(dependency: ParentComponent) :
    ViewBuilder<SelectAirportView, SelectAirportRouter, SelectAirportBuilder.ParentComponent>(
        dependency
    ) {

    /**
     * Builds a new [SelectAirportRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [SelectAirportRouter].
     */
    fun build(parentViewGroup: ViewGroup): SelectAirportRouter {
        val view = createView(parentViewGroup)
        val interactor = SelectAirportInteractor()
        val component = DaggerSelectAirportBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.selectairportRouter()
    }

    override fun inflateView(
        inflater: LayoutInflater,
        parentViewGroup: ViewGroup
    ): SelectAirportView {
        return inflater.inflate(
            R.layout.select_airport_rib,
            parentViewGroup,
            false
        ) as SelectAirportView
    }

    interface ParentComponent {
        // TODO: Define dependencies required from your parent interactor here.
    }

    @dagger.Module
    abstract class Module {

        @SelectAirportScope
        @Binds
        internal abstract fun presenter(view: SelectAirportView): SelectAirportInteractor.SelectAirportPresenter

        @dagger.Module
        companion object {

            @SelectAirportScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: SelectAirportView,
                interactor: SelectAirportInteractor
            ): SelectAirportRouter {
                return SelectAirportRouter(view, interactor, component)
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @SelectAirportScope
    @dagger.Component(
        modules = [Module::class],
        dependencies = [ParentComponent::class]
    )
    interface Component : InteractorBaseComponent<SelectAirportInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: SelectAirportInteractor): Builder

            @BindsInstance
            fun view(view: SelectAirportView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun selectairportRouter(): SelectAirportRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class SelectAirportScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class SelectAirportInternal
}