/*
 * Copyright (c) 2008, 2009, 2011 Oracle, Inc. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.  The Eclipse Public License is available
 * at http://www.eclipse.org/legal/epl-v10.html and the Eclipse Distribution License
 * is available at http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.cfgglobal.generator.metadata

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME

@Target(AnnotationTarget.FIELD)
@Retention(RUNTIME)
annotation class FieldFeature(
        val sortable: Boolean = false,
        val searchable: Boolean = false,
        val display: Boolean = true,
        val boolean: Boolean = true,
        val require: Boolean = true
)
