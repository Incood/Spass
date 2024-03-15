package com.example.passwordmanager.data

import com.example.passwordmanager.R

//object Datasource {
//    var sites: MutableList<Website> = mutableListOf()
//}

object Datasource {
    val sites = mutableListOf(
        Website(
            id = 0,
            icon = R.drawable._1,
            siteName = "Android1",
            url = "dsds4ds",
            personName = "Nick",
            password = "asd123"
        ),
        Website(
            id = 1,
            icon = R.drawable._2,
            siteName = "Android2",
            url = "dsdsd3s",
            personName = "Joe",
            password = "asd123"
        ),
        Website(
            id = 2,
            icon = R.drawable._3,
            siteName = "Android3",
            url = "dsdsd1s",
            personName = "Wanya",
            password = "asd123"
        ),
        Website(
            id = 3,
            icon = R.drawable._4,
            siteName = "Android4",
            url = "dsdsd2s",
            personName = "Sergey",
            password = "asd123"
        )
    )
}