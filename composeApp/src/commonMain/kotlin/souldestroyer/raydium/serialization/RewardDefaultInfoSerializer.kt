package souldestroyer.raydium.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import souldestroyer.raydium.model.RaydiumRewardDefaultInfo
import souldestroyer.raydium.model.RaydiumRewardMintInfo

object RewardDefaultInfoSerializer : KSerializer<RaydiumRewardDefaultInfo> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RewardDefaultInfo") {
        element<RaydiumRewardMintInfo>("mint")
        element<String>("perSecond")
        element<String?>("startTime", isOptional = true)
        element<String?>("endTime", isOptional = true)
    }

    override fun deserialize(decoder: Decoder): RaydiumRewardDefaultInfo {
        val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("Expected JsonDecoder")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return RaydiumRewardDefaultInfo(
            mint = jsonDecoder.json.decodeFromJsonElement(RaydiumRewardMintInfo.serializer(), jsonObject.getValue("mint")),
            perSecond = jsonObject.getValue("perSecond").jsonPrimitive.content,
            startTime = jsonObject["startTime"]?.jsonPrimitive?.contentOrNull,
            endTime = jsonObject["endTime"]?.jsonPrimitive?.contentOrNull
        )
    }

    override fun serialize(encoder: Encoder, value: RaydiumRewardDefaultInfo) {
        // Implement if needed for serialization, otherwise leave it empty
    }
}
