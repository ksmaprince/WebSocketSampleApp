package com.khun.websocketsample.data.mapper


interface Mapper<DataEntity, DomainModel> {

    fun mapFromEntity(entity: DataEntity): DomainModel

    fun mapToEntity(domainModel: DomainModel): DataEntity
}