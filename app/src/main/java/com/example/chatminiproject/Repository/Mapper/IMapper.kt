package com.example.chatminiproject.Repository.Mapper


interface IMapper<From, To> {

    fun map(from: From): To
}