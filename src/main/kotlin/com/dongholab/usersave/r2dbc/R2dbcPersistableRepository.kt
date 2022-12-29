package com.dongholab.usersave.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.NoRepositoryBean
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@NoRepositoryBean
interface R2dbcPersistableRepository<T: R2dbcPersistable<S>, S>: R2dbcRepository<T, S> {
}

fun <T: R2dbcPersistable<S>, S> R2dbcPersistableRepository<T, S>.save(entity: T, updatable: Boolean): Mono<T> {
    return when (updatable) {
        true -> {
            entity.id.let {
                when (it) {
                    null -> {
                        entity.setNew()
                        save(entity)
                    }
                    else -> {
                        existsById(it).flatMap {
                            if (!it) {
                                entity.setNew()
                            }
                            save(entity)
                        }
                    }
                }
            }
        }
        else -> {
            entity.setNew()
            save(entity)
        }
    }
}

fun <T: R2dbcPersistable<S>, S> R2dbcPersistableRepository<T, S>.saveAll(entities: Iterable<T>, isUpdate: Boolean): Flux<T> {
    return when (isUpdate) {
        true -> {
            val idList = entities.map { it.id }
            val isHaveFlux: Mono<Set<S>> = findAllById(idList).mapNotNull { it?.id?.let { it }?: null }.map { it!! }.collectList().map { it.toSet() }

            val updatableEntitiesFlux: Flux<T> = Flux.fromIterable(entities).flatMap { entity ->
                val isUpdate: Mono<S?> = isHaveFlux.map {
                    val check: S? = it.find {
                        entity.id == it
                    }
                    check
                }
                isUpdate.map {
                    if (it != null) {
                        entity.setNew()
                    }
                    entity
                }
            }
            saveAll(updatableEntitiesFlux)
        }
        else -> {
            // 만약 false 기입시 항상 업데이트
            val update = entities.map {
                it.setNew()
                it
            }
            saveAll(update)
        }
    }
}

fun <T: R2dbcPersistable<S>, S> R2dbcPersistableRepository<T, S>.saveAll(entities: Flux<T>, isUpdate: Boolean): Flux<T> {
    return when (isUpdate) {
        true -> {
            val idList = entities.map { it.id }
            val isHaveFlux: Mono<Set<S>> = findAllById(idList).mapNotNull { it?.id?.let { it }?: null }.map { it!! }.collectList().map { it.toSet() }

            val updatableEntitiesFlux: Flux<T> = entities.flatMap { entity ->
                val isUpdate: Mono<Boolean> = isHaveFlux.map {
                    val check: S? = it.find {
                        entity.id == it
                    }
                    check == null
                }
                isUpdate.map {
                    if (it) {
                        entity.setNew()
                    }
                    entity
                }
            }
            saveAll(updatableEntitiesFlux)
        }
        else -> {
            // 만약 false 기입시 항상 업데이트
            val update = entities.map {
                it.setNew()
                it
            }
            saveAll(update)
        }
    }
}