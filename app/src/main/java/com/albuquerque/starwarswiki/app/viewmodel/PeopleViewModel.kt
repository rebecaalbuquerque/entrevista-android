package com.albuquerque.starwarswiki.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.albuquerque.starwarswiki.app.model.ui.PersonUI
import com.albuquerque.starwarswiki.app.usecase.FavoriteUseCase
import com.albuquerque.starwarswiki.app.usecase.GetPeopleUseCase
import com.albuquerque.starwarswiki.app.usecase.GetSearchUseCase
import com.albuquerque.starwarswiki.app.view.handler.PersonHandler
import com.albuquerque.starwarswiki.core.mediator.SingleMediatorLiveData
import com.albuquerque.starwarswiki.core.network.StringWrapper
import com.albuquerque.starwarswiki.core.network.WikiResult
import com.albuquerque.starwarswiki.core.viewmodel.WikiViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PeopleViewModel(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val favoriteUseCase: FavoriteUseCase,
    private val getSearchUseCase: GetSearchUseCase
): WikiViewModel(), PersonHandler {

    var people: SingleMediatorLiveData<List<PersonUI>> = SingleMediatorLiveData()

    val onHandleFavorite = MutableLiveData<Pair<Int?, String>>()

    init {
        getPeoples()
    }

    fun getPeoples() {
        getPeopleUseCase(false).onEach {
            people.emit(it)

        }.catch { error ->

            onError.value = StringWrapper(error.message ?: "Erro ao realizar requisição")

        }.launchIn(viewModelScope)
    }

    fun search(search: String) {

        viewModelScope.launch {
            val result = getSearchUseCase.invoke(search)

            when (result) {

                is WikiResult.Success -> people.value = result.data

                is WikiResult.Failure -> onError.value = result.error.errorMessage

            }

        }

    }

    fun clearPeople() { people.value = listOf() }

    override fun handleFavorite(person: PersonUI, position: Int) {

        viewModelScope.launch {
            val result = favoriteUseCase.invoke(person)

            when (result) {

                is WikiResult.Success -> {
                    onHandleFavorite.value = Pair(position, result.data!!)
                }

                is WikiResult.Failure -> {
                    onHandleFavorite.value = Pair(null, result.error.message ?: "Erro")
                }

            }

        }


    }
}