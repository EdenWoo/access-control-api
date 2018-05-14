package com.github.leon.generator.metadata

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelFeature(val importable:Boolean, val exportable:Boolean)


