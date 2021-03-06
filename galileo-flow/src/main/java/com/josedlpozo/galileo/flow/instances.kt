/*
 * Copyright (C) 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.flow

import arrow.core.ForTry
import arrow.core.Try
import arrow.instances.`try`.monadError.monadError
import com.josedlpozo.galileo.flow.algebras.DefaultFlowEventDataSource
import com.josedlpozo.galileo.flow.algebras.DefaultFlowEventUseCase
import com.josedlpozo.galileo.flow.algebras.FlowEventUseCase


internal interface FlowEventInstances<F> {

    val dataSource: DefaultFlowEventDataSource<F>

    val flowLifeCycleCallback: FlowLifeCycleCallback<F>

    val useCase: FlowEventUseCase<F>
}

internal object FlowEventTry :
    FlowEventInstances<ForTry> {

    override val dataSource = DefaultFlowEventDataSource(Try.monadError())

    override val flowLifeCycleCallback = FlowLifeCycleCallback(dataSource)

    override val useCase = DefaultFlowEventUseCase(dataSource)

}