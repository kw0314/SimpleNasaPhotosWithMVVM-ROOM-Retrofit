package com.mingolab.myapplication.view


enum class Screens {
    PhotoList,
    PhotoDetail
}

class ScreenHandler {
    var curScreen = Screens.valueOf("PhotoList")

    public fun getScreen():String{
        return curScreen.name
    }

    public fun setScreen(screen: String){
        curScreen = Screens.valueOf(screen)
    }

    public fun switchScreen(): String {
        curScreen = when (curScreen){
            Screens.PhotoList -> Screens.PhotoDetail
            Screens.PhotoDetail -> Screens.PhotoList
        }
        return curScreen.name
    }
}