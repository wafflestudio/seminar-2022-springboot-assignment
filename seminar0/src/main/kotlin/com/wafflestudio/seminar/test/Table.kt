package com.wafflestudio.seminar.test

import java.util.*

class Table(initString : String?) {
    private var currentElement : Element
    private val elements = mutableListOf<Element>()
    private val restoreStack = Stack<Element>();
    init {
        if(initString != null)
        {
            val varInitString = initString.removeSurrounding("[\"", "\"]")
//            println(varInitString)
            val splitString = varInitString.split("\",\"")
            for(s:String in splitString)
            {
                elements.add(Element(s))
            }
        }
        currentElement = elements[0]
    }
    fun delete()
    {
        val i = elements.indexOf(currentElement)
        currentElement.index = i
//        print("index: " + i + " count: " + elements.count())
        restoreStack.push(currentElement)
        currentElement = if(i == elements.count() - 1) { elements[elements.count() - 2] }
        else{
            elements[i + 1]
        }
        elements.removeAt(i)
    }
    
    fun printAll()
    {
        for(e :Element in elements)
        {
            println(e.name)
        }
    }
    
    fun moveIndex(mov: Int) : Boolean
    {
        val newIndex = elements.indexOf(currentElement) + mov
//        println("new index: $newIndex")
        if(newIndex < 0 || elements.count() <= newIndex)
        {
            return false
        }
        currentElement = elements[newIndex]
        return true
    }
    
    fun restore() : Boolean
    {
        if(restoreStack.empty())
            return false
        val restored = restoreStack.pop()
        elements.add(restored.index, restored)
        return true
    }
}