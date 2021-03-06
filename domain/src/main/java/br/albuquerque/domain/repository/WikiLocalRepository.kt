package br.albuquerque.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import br.albuquerque.core.repository.WikiDataSource
import br.albuquerque.data.dao.WikiDAO
import br.albuquerque.data.entity.ConfigEntity
import br.albuquerque.data.entity.PersonEntity

class WikiLocalRepository(
    private val wikiDao: WikiDAO
): WikiDataSource(), IWikiLocalDataSource {

    override fun getPeople(isFavorite: Boolean): LiveData<List<PersonEntity>> {

        return if(!isFavorite)
            wikiDao.getPeople().distinctUntilChanged()
        else
            wikiDao.getOnlyPeopleFavorited().distinctUntilChanged()
    }

    override suspend fun savePeoples(people: List<PersonEntity>, shouldClearTable: Boolean) {
        wikiDao.saveAllTransaction(people, shouldClearTable)
    }

    override fun getTryAgainPeople(): LiveData<MutableList<PersonEntity>> = wikiDao.getOnlyPeopleTryAgain()

    override suspend fun updatePerson(person: PersonEntity) {
        wikiDao.updatePerson(person)
    }

    override suspend fun getPeopleSuspend(): List<PersonEntity> {
        return wikiDao.getPeopleSuspend()
    }

    override suspend fun saveConfig(config: ConfigEntity) {
        wikiDao.save(config)
    }

    override fun getConfig(): LiveData<ConfigEntity?> = wikiDao.getConfig()
}