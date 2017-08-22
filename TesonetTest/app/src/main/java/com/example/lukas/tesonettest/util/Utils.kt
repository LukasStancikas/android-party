package com.example.lukas.tesonettest.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Modifier

/**
 * Created by lukas on 17.8.22.
 */

val gson: Gson by lazy {
	val builder = GsonBuilder()
	builder.excludeFieldsWithModifiers(Modifier.TRANSIENT)
	builder.excludeFieldsWithModifiers(Modifier.STATIC)
	builder.create()
}