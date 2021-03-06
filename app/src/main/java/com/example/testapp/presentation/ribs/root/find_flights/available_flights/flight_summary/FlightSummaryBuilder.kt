package com.example.testapp.presentation.ribs.root.find_flights.available_flights.flight_summary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.domain.entity.flight.FlightOption
import com.example.domain.mapper.Mapper
import com.example.testapp.R
import com.example.testapp.entity.FlightSummaryVM
import com.example.testapp.mapper.FlightOptionToFlightSummariesListVMMapper
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Scope

/**
 * Builder for the {@link FlightSummaryScope}.
 */
class FlightSummaryBuilder(dependency: ParentComponent) :
    ViewBuilder<FlightSummaryView, FlightSummaryRouter, FlightSummaryBuilder.ParentComponent>(
        dependency
    ) {

    /**
     * Builds a new [FlightSummaryRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [FlightSummaryRouter].
     */
    fun build(parentViewGroup: ViewGroup, flightOption: FlightOption): FlightSummaryRouter {
        val view = createView(parentViewGroup)
        val interactor = FlightSummaryInteractor()
        val component = DaggerFlightSummaryBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .flightOption(flightOption)
            .build()
        return component.flightsummaryRouter()
    }

    override fun inflateView(
        inflater: LayoutInflater,
        parentViewGroup: ViewGroup,
    ): FlightSummaryView {
        return inflater.inflate(
            R.layout.flight_summary_rib,
            parentViewGroup,
            false
        ) as FlightSummaryView
    }

    interface ParentComponent {
        fun flightSummaryListener(): FlightSummaryInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @FlightSummaryScope
        @Binds
        internal abstract fun presenter(view: FlightSummaryView): FlightSummaryInteractor.FlightSummaryPresenter

        @dagger.Module
        companion object {

            @FlightSummaryScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: FlightSummaryView,
                interactor: FlightSummaryInteractor,
            ): FlightSummaryRouter {
                return FlightSummaryRouter(view, interactor, component)
            }

            @FlightSummaryScope
            @Provides
            @JvmStatic
            internal fun flightOptionToSummaryMapper(): Mapper<FlightOption, List<FlightSummaryVM>> =
                FlightOptionToFlightSummariesListVMMapper()
        }
    }

    @FlightSummaryScope
    @dagger.Component(
        modules = [Module::class],
        dependencies = [ParentComponent::class]
    )
    interface Component : InteractorBaseComponent<FlightSummaryInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: FlightSummaryInteractor): Builder

            @BindsInstance
            fun view(view: FlightSummaryView): Builder

            @BindsInstance
            fun flightOption(flightOption: FlightOption): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun flightsummaryRouter(): FlightSummaryRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class FlightSummaryScope
}
