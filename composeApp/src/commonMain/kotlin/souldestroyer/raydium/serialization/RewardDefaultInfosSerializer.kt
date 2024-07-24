package souldestroyer.raydium.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

object RewardDefaultInfosSerializer : KSerializer<List<JsonObject>> {
    override val descriptor: SerialDescriptor = JsonArray.serializer().descriptor

    override fun deserialize(decoder: Decoder): List<JsonObject> {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return when (element) {
            is JsonArray -> element.mapNotNull { it as? JsonObject }
            else -> emptyList()
        }
    }

    override fun serialize(encoder: Encoder, value: List<JsonObject>) {
        encoder.encodeSerializableValue(JsonArray.serializer(), JsonArray(value))
    }
}