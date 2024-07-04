import com.xkcddatahub.data.processor.application.usecases.IndexDataUseCase
import io.vertx.core.AbstractVerticle

// package com.xkcddatahub.data.processor.adapters.input.broker.kafka
//
// import com.xkcddatahub.data.processor.application.usecases.IndexDataUseCase
// import com.xkcddatahub.data.processor.domain.entity.DataRecord
// import io.vertx.core.AbstractVerticle
// import io.vertx.core.Promise
// import io.vertx.kafka.client.consumer.KafkaConsumer
// import io.vertx.kotlin.coroutines.await
// import io.vertx.kotlin.coroutines.dispatcher
// import kotlinx.coroutines.launch
//
class KafkaConsumerVerticle(
    private val indexDataUseCase: IndexDataUseCase,
) : AbstractVerticle() {
    override fun start() {
        super.start()
    }
//    override fun start(startPromise: Promise<Void>) {
//        val config =
//            mapOf(
//                "bootstrap.servers" to "localhost:9092",
//                "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
//                "value.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
//                "group.id" to "data-processor-group",
//                "auto.offset.reset" to "earliest",
//                "enable.auto.commit" to "false",
//            )
//
//        val consumer = KafkaConsumer.create<String, String>(vertx, config)
//
//        consumer.handler { record ->
//            val dataRecord = DataRecord(record.key(), record.value())
//            vertx.launch(vertx.dispatcher()) {
//                indexDataUseCase.execute(dataRecord)
//                consumer.commit().await()
//            }
//        }
//
//        consumer.subscribe("data-topic")
//            .onSuccess { startPromise.complete() }
//            .onFailure { startPromise.fail(it) }
//    }
}
