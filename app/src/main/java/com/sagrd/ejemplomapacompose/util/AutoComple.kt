package com.sagrd.ejemplomapacompose.util

//
//typealias CustomFiler<T> = (T,String) -> Boolean
//
//fun <T> List<T>.isAutoCompleteEntities(filter:CustomFiler<T>):List<CuidadesAutoComplete<T>>{
//    return map{
//        object : CuidadesAutoComplete<T>{
//            override val value: T
//                get() = it
//
//            override fun filtro(query: String): Boolean {
//                return filter(value,query)
//            }
//        }
//    }
//}